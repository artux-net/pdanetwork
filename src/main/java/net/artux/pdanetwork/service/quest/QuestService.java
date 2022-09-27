package net.artux.pdanetwork.service.quest;

import net.artux.pdanetwork.models.quest.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

public interface QuestService {

    Stage getStage(long storyId, long chapterId, long stageId);

    HashMap<String, List<String>> getActionsOfStage(long storyId, long chapterId, long stageId);

    Chapter getChapter(long storyId, long chapterId);

    GameMap getMap(long storyId, long mapId);

    Story getStory(long storyId);

    void updateStories();

    List<Story> getStories();

    List<StoryDto> getStoriesDto();

    Instant getReadTime();
}
