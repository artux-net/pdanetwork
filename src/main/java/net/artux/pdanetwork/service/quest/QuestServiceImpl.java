package net.artux.pdanetwork.service.quest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.quest.ChapterDto;
import net.artux.pdanetwork.models.quest.GameMap;
import net.artux.pdanetwork.models.quest.QuestMapper;
import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.models.quest.StoryDto;
import net.artux.pdanetwork.models.quest.StoryInfo;
import net.artux.pdanetwork.models.quest.admin.StoriesStatus;
import net.artux.pdanetwork.models.quest.stage.Stage;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.util.S3Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestServiceImpl implements QuestService {

    private final QuestMapper questMapper;
    private final UserService userService;
    private final S3Service s3Service;
    private final ObjectMapper objectMapper;

    private final Map<Long, Story> storiesCache = new HashMap<>();
    private final Map<Long, StoryDto> stories = new HashMap<>();
    private final Map<Role, List<StoryDto>> roleStories = new HashMap<>();
    private final Map<UUID, StoryDto> usersStories = new HashMap<>();
    private Instant updatedTime;
    private long lastStoryId = -2;

    @Override
    public Status setUserStory(Story story) {
        story.setId(lastStoryId + 1);
        usersStories.put(userService.getCurrentId(), questMapper.dto(story));
        return new Status(true, "История загружена. Сбросьте кэш для появления.");
    }

    @Override
    public Status setPublicStory(Story story) {
        return addStories(List.of(story));
    }

    @Override
    public Story getOriginalStory(long storyId) {
        return storiesCache.get(storyId);
    }

    @Override
    public Stage getStage(long storyId, long chapterId, long stageId) {
        return getChapter(storyId, chapterId).getStage(stageId);
    }

    @Override
    public Map<String, List<String>> getActionsOfStage(long storyId, long chapterId, long stageId) {
        return getStage(storyId, chapterId, stageId).getActions();
    }

    @Override
    public ChapterDto getChapter(long storyId, long chapterId) {
        return getStory(storyId).getChapters().get(chapterId);
    }

    @Override
    public GameMap getMap(long storyId, long mapId) {
        return getStory(storyId).getMaps().get(mapId);
    }

    @Override
    public StoryDto getStory(long storyId) {
        UserEntity user = userService.getUserById();
        StoryDto story = stories.get(storyId);
        if (story == null)
            story = usersStories.get(user.getId());
        if (story == null)
            throw new RuntimeException();
        if (story.getAccess().getPriority() > user.getRole().getPriority())
            throw new AccessDeniedException("User has no access to the story");
        return story;
    }

    @Override
    public Status addStories(Collection<Story> stories) {
        for (var story : stories) {
            if (story.getId() > lastStoryId)
                lastStoryId = story.getId();

            storiesCache.put(story.getId(), story);
            try {
                s3Service.putString("story-" + story.getId(), objectMapper.writeValueAsString(story));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            this.stories.put(story.getId(), questMapper.dto(story));
        }

        for (Role role : Role.values()) {
            List<StoryDto> roleStories = new LinkedList<>();
            for (var story : this.stories.values()) {
                if (story.getAccess().getPriority() <= role.getPriority())
                    roleStories.add(story);
            }
            this.roleStories.put(role, roleStories);
        }
        updatedTime = Instant.now();
        return new Status(true, "Истории обновлены.");
    }

    @Override
    public Collection<StoryDto> getStories() {
        Role role = userService.getUserById().getRole();
        return roleStories.get(role);
    }

    @Override
    public Collection<StoryInfo> getStoriesInfo() {
        return questMapper.info(getStories());
    }

    @Override
    public StoriesStatus getStatus() {
        return StoriesStatus.builder()
                .readTime(updatedTime)
                .userStories(usersStories.size())
                .stories(questMapper.adminInfo(stories.values()))
                .build();
    }
}
