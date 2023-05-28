package net.artux.pdanetwork.service.quest;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.quest.Chapter;
import net.artux.pdanetwork.models.quest.GameMap;
import net.artux.pdanetwork.models.quest.QuestMapper;
import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.models.quest.StoryInfo;
import net.artux.pdanetwork.models.quest.admin.StoriesStatus;
import net.artux.pdanetwork.models.quest.stage.Stage;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.service.user.UserService;
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
    private Instant updatedTime;
    private Map<Long, Story> stories = new HashMap<>();
    private Map<Role, List<Story>> roleStories = new HashMap<>();
    private final Map<UUID, Story> usersStories = new HashMap<>();
    private long lastStoryId = -2;

    @Override
    public Status setUserStory(Story story) {
        story.setId(lastStoryId + 1);
        usersStories.put(userService.getCurrentId(), story);
        return new Status(true, "История загружена. Сбросьте кэш для появления.");
    }

    @Override
    public Stage getStage(long storyId, long chapterId, long stageId) {
        return getChapter(storyId, chapterId).getStage(stageId);
    }

    @Override
    public HashMap<String, List<String>> getActionsOfStage(long storyId, long chapterId, long stageId) {
        return getStage(storyId, chapterId, stageId).getActions();
    }

    @Override
    public Chapter getChapter(long storyId, long chapterId) {
        return getStory(storyId).getChapters().get(chapterId);
    }

    @Override
    public GameMap getMap(long storyId, long mapId) {
        return getStory(storyId).getMaps().get(mapId);
    }

    @Override
    public Story getStory(long storyId) {
        UserEntity user = userService.getUserById();
        Story story = stories.get(storyId);
        if (story == null)
            story = usersStories.get(user.getId());
        if (story.getAccess().getPriority() > user.getRole().getPriority())
            throw new AccessDeniedException("User has no access to the story");
        return story;
    }

    @Override
    public Status setStories(Collection<Story> stories) {
        this.stories = new HashMap<>();
        for (var story : stories) {
            if (story.getId() > lastStoryId)
                lastStoryId = story.getId();

            this.stories.put(story.getId(), story);
        }

        roleStories = new HashMap<>();
        for (Role role : Role.values()) {
            List<Story> roleStories = new LinkedList<>();
            for (var story : stories) {
                if (story.getAccess().getPriority() <= role.getPriority())
                    roleStories.add(story);
            }
            this.roleStories.put(role, roleStories);
        }
        updatedTime = Instant.now();
        return null;
    }

    @Override
    public Collection<Story> getStories() {
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
