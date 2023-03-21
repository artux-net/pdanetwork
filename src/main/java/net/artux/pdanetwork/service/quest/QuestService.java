package net.artux.pdanetwork.service.quest;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.quest.Chapter;
import net.artux.pdanetwork.models.quest.GameMap;
import net.artux.pdanetwork.models.quest.Stage;
import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.models.quest.StoryDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

public interface QuestService {

    Stage getStage(long storyId, long chapterId, long stageId);

    HashMap<String, List<String>> getActionsOfStage(long storyId, long chapterId, long stageId);

    Chapter getChapter(long storyId, long chapterId);

    GameMap getMap(long storyId, long mapId);

    Story getStory(long storyId);

    Status readStories();

    Status saveStories(MultipartFile storiesArchive, String token);

    List<Story> getStories();

    List<StoryDto> getStoriesDto();

    Instant getReadTime();
}
