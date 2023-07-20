package net.artux.pdanetwork.service.quest;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.quest.ChapterDto;
import net.artux.pdanetwork.models.quest.GameMap;
import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.models.quest.StoryDto;
import net.artux.pdanetwork.models.quest.StoryInfo;
import net.artux.pdanetwork.models.quest.admin.StoriesStatus;
import net.artux.pdanetwork.models.quest.stage.Stage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface QuestService {

    Stage getStage(long storyId, long chapterId, long stageId);

    Map<String, List<String>> getActionsOfStage(long storyId, long chapterId, long stageId);

    ChapterDto getChapter(long storyId, long chapterId);

    GameMap getMap(long storyId, long mapId);

    StoryDto getStory(long storyId);

    Status reloadPublicStories(Collection<Story> stories);

    Collection<StoryInfo> getPublicStories();

    Collection<StoryInfo> getCommunityStories();

    StoriesStatus getStatus();

    Status setUserStory(Story story, String message);

    Status setUserStory(UUID backupId);

    Status setPublicStory(Story story, String message);

}
