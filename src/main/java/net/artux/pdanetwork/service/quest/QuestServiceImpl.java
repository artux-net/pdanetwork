package net.artux.pdanetwork.service.quest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.file.File;
import net.artux.pdanetwork.models.quest.*;
import net.artux.pdanetwork.service.util.ValuesService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class QuestServiceImpl implements QuestService {

    private final ObjectMapper objectMapper;
    private final QuestMapper questMapper;

    private final ValuesService valuesService;
    private final Logger logger;
    private HashMap<Long, Story> stories;
    private Instant readTime;

    @PostConstruct
    @Override
    public void updateStories() {
        String fileServerUrl = valuesService.getStoryFilesUrl() + "";
        File[] storyFiles = new File[0];
        try {
            storyFiles = objectMapper.readValue(
                    new URL(fileServerUrl), File[].class);
        } catch (IOException e) {
            logger.error("Can not connect to files server.", e);
        }

        HashMap<Long, Story> stories = new HashMap<>();
        for (var storyFile : storyFiles) {
            if (storyFile.getType() == File.Type.directory) {
                String storyUrl = fileServerUrl + "/" + storyFile.getName();
                try {
                    Story story = objectMapper.readValue(
                            new URL(storyUrl + "/info.json"), Story.class);

                    //chapters
                    HashMap<Long, Chapter> chapters = new HashMap<>();
                    File[] chapterFiles = objectMapper.readValue(
                            new URL(storyUrl), File[].class);
                    for (var chapterFile : chapterFiles) {
                        if (!(chapterFile.getType() == File.Type.directory
                                || chapterFile.getName().toLowerCase(Locale.ROOT).contains("info"))) {
                            Chapter chapter =
                                    objectMapper.readValue(
                                            new URL(storyUrl + "/" + chapterFile.getName()), Chapter.class);
                            chapters.put(chapter.getId(), chapter);
                        }
                    }
                    story.setChapters(chapters);

                    //maps
                    HashMap<Long, GameMap> maps = new HashMap<>();
                    File[] mapFiles = objectMapper.readValue(
                            new URL(storyUrl + "/maps"), File[].class);
                    for (var mapFile : mapFiles) {
                        if (mapFile.getType() == File.Type.file) {
                            GameMap gameMap =
                                    objectMapper.readValue(
                                            new URL(storyUrl + "/maps/" + mapFile.getName()), GameMap.class);
                            maps.put(gameMap.getId(), gameMap);
                        }
                    }
                    story.setMaps(maps);

                    stories.put(story.getId(), story);
                } catch (IOException e) {
                    logger.error("Error reading stories", e);
                }

            }
        }
        if (stories.values().size() > 0) {
            this.stories = stories;
            readTime = Instant.now();
            logger.info("Stories updated, count: {}", stories.values().size());
        }
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
        return stories.get(storyId);
    }

    @Override
    public List<Story> getStories() {
        return stories.values().stream().toList();
    }

    @Override
    public List<StoryDto> getStoriesDto() {
        return questMapper.dto(getStories());
    }

    @Override
    public Instant getReadTime() {
        return readTime;
    }
}
