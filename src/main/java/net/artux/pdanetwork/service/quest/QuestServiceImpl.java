package net.artux.pdanetwork.service.quest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.quest.*;
import net.artux.pdanetwork.models.quest.workflow.Trigger;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.util.ValuesService;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestServiceImpl implements QuestService {

    private final ObjectMapper objectMapper;
    private final QuestMapper questMapper;
    private final UserService userService;
    private final ValuesService valuesService;
    private final Logger logger;
    private HashMap<Long, Story> stories;
    private Instant readTime;
    private HashMap<Role, List<Story>> roleStories;
    private final HashMap<UUID, Story> usersStories = new HashMap<>();
    private long lastStoryId = -2;
    private Exception lastException;

    @PostConstruct
    public Status downloadStories() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + valuesService.getWebhookToken());

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(valuesService.getStoriesWebhookAddress());

        HttpEntity<Trigger> entity = new HttpEntity<>(headers);

        logger.info("Download stories from " + valuesService.getStoriesWebhookAddress());
        HttpEntity<byte[]> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                byte[].class);

        try {
            return saveStories(new ByteArrayInputStream(response.getBody()));
        } catch (IOException e) {
            logger.error("Downloading stories error ", e);
            logger.info("Server answer: " + response);
            return new Status(false, "Downloading stories error");
        }
    }

    public Status readStories(File file) {
        File[] storiesDirs = file.listFiles();
        if (storiesDirs == null)
            return new Status(false, "Empty stories folder.");

        if (storiesDirs.length == 1)
            return readStories(storiesDirs[0]);

        int errors = 0;
        HashMap<Long, Story> stories = new HashMap<>();
        for (var storyDir : storiesDirs) {
            if (storyDir.isFile() || storyDir.getName().contains("."))
                continue;

            try {
                Story story = objectMapper.readValue(
                        new File(storyDir + "/info.json"), Story.class);

                //chapters
                HashMap<Long, Chapter> chapters = new HashMap<>();
                File[] chapterFiles = storyDir.listFiles();
                if (chapterFiles == null)
                    continue;

                for (var chapterFile : chapterFiles) {
                    String filename = chapterFile.getName().toLowerCase(Locale.ROOT);
                    if (chapterFile.isDirectory()
                            || filename.contains("info.json")
                            || filename.contains("mission.json"))
                        continue;

                    try {
                        Chapter chapter = objectMapper.readValue(chapterFile, Chapter.class);
                        chapters.put(chapter.getId(), chapter);
                    } catch (IOException e) {
                        errors++;
                        logger.error("Error while reading chapter " + chapterFile.getName(), e);
                        lastException = e;
                    }

                }
                story.setChapters(chapters);

                //maps
                HashMap<Long, GameMap> maps = new HashMap<>();
                File[] mapFiles = new File(storyDir + "/maps").listFiles();
                if (mapFiles == null)
                    continue;

                for (var mapFile : mapFiles) {
                    try {
                        GameMap gameMap = objectMapper.readValue(mapFile, GameMap.class);
                        maps.put(gameMap.getId(), gameMap);
                    } catch (IOException e) {
                        errors++;
                        logger.error("Error while reading map " + mapFile.getName(), e);
                        lastException = e;
                    }
                }
                story.setMaps(maps);

                try {
                    Mission[] missions = objectMapper.readValue(
                            new File(storyDir + "/missions.json"), Mission[].class);
                    story.setMissions(Arrays.stream(missions).toList());
                } catch (IOException ignored) {
                }
                if (story.getId() > lastStoryId) {
                    lastStoryId = story.getId();
                }
                stories.put(story.getId(), story);
            } catch (IOException e) {
                errors++;
                logger.error("Error while reading story " + storyDir.getName(), e);
                lastException = e;
            }
        }
        deleteDirectory(file);
        if (stories.values().size() > 0) {
            this.stories = stories;
            readTime = Instant.now();
            logger.info("Stories updated, count: {}", stories.values().size());
            return new Status(true, "Stories updated, errors " + errors);
        }

        logger.warn("Stories not found at " + valuesService.getStoriesDirectory());
        return new Status(false, "Stories not updated, errors " + errors + ", last exception: " + lastException.getMessage());
    }

    @Override
    public Status setStories(MultipartFile storiesArchive) {
        if (userService.getUserById().getRole() != Role.ADMIN)
            return new Status(false, "Wrong role.");

        try {
            return saveStories(storiesArchive.getInputStream());
        } catch (IOException e) {
            logger.error("Error while saving stories. ", e);
            return new Status(false, "Could not save stories, update failed.");
        }
    }

    @Override
    public Status setUserStory(Story story) {
        story.setId(lastStoryId + 1);
        usersStories.put(userService.getCurrentId(), story);
        return new Status(true, "История загружена. Сбросьте кэш для появления.");
    }

    private Status saveStories(InputStream inputStream) throws IOException {
        File zip = File.createTempFile(UUID.randomUUID().toString(), "temp");
        FileOutputStream o = new FileOutputStream(zip);
        IOUtils.copy(inputStream, o);
        o.close();

        ZipFile zipFile = new ZipFile(zip);
        zipFile.extractAll(valuesService.getStoriesDirectory());

        Status status = readStories(new File(valuesService.getStoriesDirectory()));

        zip.delete();
        return status;
    }

    static public void deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null)
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
        }
        path.delete();
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
    public Collection<Story> getStories() {
        int priority = userService.getUserById().getRole().getPriority();
        Set<Story> storyList = stories.values().stream().filter(story -> story.getAccess().getPriority() <= priority).collect(Collectors.toSet());
        if (usersStories.containsKey(userService.getCurrentId()))
            storyList.add(usersStories.get(userService.getCurrentId()));
        return storyList;
    }

    @Override
    public Collection<StoryDto> getStoriesDto() {
        return questMapper.dto(getStories());
    }

    @Override
    public Instant getReadTime() {
        return readTime;
    }
}
