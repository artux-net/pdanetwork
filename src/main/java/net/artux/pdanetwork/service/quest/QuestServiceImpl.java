package net.artux.pdanetwork.service.quest;

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
import net.artux.pdanetwork.utills.security.AdminAccess;
import net.artux.pdanetwork.utills.security.CreatorAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.EnumMap;
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
    private final QuestBackupService questBackupService;

    private final Map<Long, StoryDto> stories = new HashMap<>();
    private final Map<Role, List<StoryDto>> roleStories = new EnumMap<>(Role.class);
    private final Map<UUID, StoryDto> usersStories = new HashMap<>();
    private Instant updatedTime;
    private long lastStoryId = -2;
    private Logger logger = LoggerFactory.getLogger(QuestServiceImpl.class);

    @Override
    @CreatorAccess
    public Status setUserStory(Story story, String message) {
        story.setId(lastStoryId + 1);
        usersStories.put(userService.getCurrentId(), questMapper.dto(story));
        questBackupService.saveStory(story, message);
        return new Status(true, "История загружена, размещена в архиве. Сбросьте кэш пда для появления.");
    }

    @Override
    @CreatorAccess
    public Status setUserStory(UUID backupId) {
        Story story = questBackupService.getBackup(backupId);
        usersStories.put(userService.getCurrentId(), questMapper.dto(story));
        return new Status(true, "Пользовательская история взята из хранилища и установлена текущей.");
    }

    @Override
    @AdminAccess
    public Status setPublicStory(Story story, String message) {
        //questBackupService.saveStory(story, StoryType.PUBLIC, message);
        return reloadPublicStories(List.of(story));
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
            story = questMapper.dto(questBackupService.getCommunityStory(storyId));
        if (story == null)
            throw new RuntimeException();
        if (story.getAccess().getPriority() > user.getRole().getPriority())
            throw new AccessDeniedException("User has no access to the story");
        return story;
    }

    @Override
    public Status reloadPublicStories(Collection<Story> stories) {
        int counter = 0;
        for (var story : stories) {
            if (story.getId() > lastStoryId)
                lastStoryId = story.getId();
            this.stories.put(story.getId(), questMapper.dto(story));
            counter++;
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
        String message = "Установлены публичные истории, количество: " + counter;
        logger.info(message);
        return new Status(true, message);
    }

    private Collection<StoryDto> getStories(UserEntity user) {
        Role role = user.getRole();
        return roleStories.get(role);
    }

    @Override
    public Collection<StoryInfo> getPublicStories() {
        UserEntity user = userService.getUserById();
        Collection<StoryInfo> storiesInfo = questMapper.info(getStories(user));
        StoryInfo userStory = questMapper.info(usersStories.get(user.getId()));
        if (userStory != null) storiesInfo.add(userStory);
        return storiesInfo;
    }

    @Override
    public Collection<StoryInfo> getCommunityStories() {
        return questMapper.info(questMapper.dto(questBackupService.getCommunityStories()));
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
