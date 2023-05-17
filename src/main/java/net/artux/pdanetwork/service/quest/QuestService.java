package net.artux.pdanetwork.service.quest;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.quest.*;
import net.artux.pdanetwork.models.quest.admin.StoriesStatus;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface QuestService {

    Stage getStage(long storyId, long chapterId, long stageId);

    HashMap<String, List<String>> getActionsOfStage(long storyId, long chapterId, long stageId);

    Chapter getChapter(long storyId, long chapterId);

    GameMap getMap(long storyId, long mapId);

    Story getStory(long storyId);

    Status downloadStories();

    Status setStories(MultipartFile storiesArchive);

    Status setUserStory(Story story);
    Collection<Story> getStories();

    Collection<StoryDto> getStoriesDto();

    Instant getReadTime();

    StoriesStatus getStatus();
}
