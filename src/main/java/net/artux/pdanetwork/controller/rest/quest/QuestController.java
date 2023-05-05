package net.artux.pdanetwork.controller.rest.quest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.quest.Chapter;
import net.artux.pdanetwork.models.quest.GameMap;
import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.models.quest.StoryDto;
import net.artux.pdanetwork.service.quest.QuestService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Tag(name = "Сюжет")
@RequestMapping("/quest")
public class QuestController {

    private final QuestService questService;

    @GetMapping("/{id}")
    public Story getStory(@PathVariable("id") long id) {
        return questService.getStory(id);
    }

    @GetMapping
    public Collection<StoryDto> getStories() {
        return questService.getStoriesDto();
    }

    @GetMapping("/{storyId}/{chapterId}")
    public Chapter getChapter(@PathVariable("storyId") long storyId, @PathVariable("chapterId") long chapterId) {
        return questService.getChapter(storyId, chapterId);
    }

    @GetMapping("/maps/{storyId}/{mapId}")
    public GameMap getMap(@PathVariable("storyId") long storyId, @PathVariable("mapId") long mapId) {
        return questService.getMap(storyId, mapId);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Status uploadStories(@RequestPart("file") MultipartFile file) {
        return questService.setStories(file);
    }

    @PostMapping(value = "/uploadCustom")
    public Status uploadCustomStory(Story story) {
        return questService.setUserStory(story);
    }
}