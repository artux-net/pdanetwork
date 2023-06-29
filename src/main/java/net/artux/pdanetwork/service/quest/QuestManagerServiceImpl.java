package net.artux.pdanetwork.service.quest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.quest.Chapter;
import net.artux.pdanetwork.models.quest.GameMap;
import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.models.quest.map.MapEnum;
import net.artux.pdanetwork.models.quest.workflow.Trigger;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.util.S3Service;
import net.artux.pdanetwork.service.util.ValuesService;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestManagerServiceImpl implements QuestManagerService {

    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final ValuesService valuesService;
    private final Logger logger;
    private final QuestService questService;
    private final S3Service s3Service;

    @PostConstruct
    public void initFromR2() {
        List<Story> stories = new LinkedList<>();
        for (String name : s3Service.getEntries("story-")) {
            try {
                stories.add(objectMapper.readValue(s3Service.getString(name), Story.class));
            } catch (JsonProcessingException e) {
                logger.error("Reading story error " + name, e);
            }
        }
        questService.addStories(stories);
        logger.info("Stories updated from r2, count: {}", stories.size());
    }


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
                            || filename.contains("missions.json"))
                        continue;

                    try {
                        Chapter chapter = objectMapper.readValue(chapterFile, Chapter.class);
                        chapters.put(chapter.getId(), chapter);
                    } catch (IOException e) {
                        errors++;
                        logger.error("Error while reading chapter " + chapterFile.getName(), e);
                    }

                }
                story.setChapters(chapters.values());

                //maps
                HashMap<Long, GameMap> maps = new HashMap<>();
                for (MapEnum enumGetter : MapEnum.values())
                    maps.put((long) enumGetter.getId(), new GameMap(enumGetter));

                File[] mapFiles = new File(storyDir + "/maps").listFiles();
                if (mapFiles == null)
                    continue;


                for (var mapFile : mapFiles) {
                    try {
                        GameMap gameMap = objectMapper.readValue(mapFile, GameMap.class);
                        maps.get(gameMap.getId()).getSpawns().addAll(gameMap.getSpawns());
                        maps.get(gameMap.getId()).getPoints().addAll(gameMap.getPoints());

                    } catch (IOException e) {
                        errors++;
                        logger.error("Error while reading map " + mapFile.getName(), e);
                    }
                }
                story.setMaps(maps.values());
                stories.put(story.getId(), story);
            } catch (IOException e) {
                errors++;
                logger.error("Error while reading story " + storyDir.getName(), e);
            }
        }
        deleteDirectory(file);
        if (stories.values().size() > 0) {
            questService.addStories(stories.values());

            logger.info("Stories updated from github, count: {}", stories.values().size());
            return new Status(true, "Stories updated, errors " + errors);
        }

        logger.warn("Stories not found at " + valuesService.getStoriesDirectory());
        return new Status(false, "Stories not updated, errors " + errors);
    }

    @Override
    public Status uploadStories(MultipartFile storiesArchive) {
        if (userService.getUserById().getRole() != Role.ADMIN)
            return new Status(false, "Wrong role.");

        try {
            return saveStories(storiesArchive.getInputStream());
        } catch (IOException e) {
            logger.error("Error while saving stories. ", e);
            return new Status(false, "Could not save stories, update failed.");
        }
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

}
