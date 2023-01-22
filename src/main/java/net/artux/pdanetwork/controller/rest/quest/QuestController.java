package net.artux.pdanetwork.controller.rest.quest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.quest.Chapter;
import net.artux.pdanetwork.models.quest.GameMap;
import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.models.quest.StoryDto;
import net.artux.pdanetwork.service.quest.QuestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<StoryDto> getStories() {
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
}