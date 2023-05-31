package net.artux.pdanetwork.controller.rest.quest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.quest.ChapterDto;
import net.artux.pdanetwork.models.quest.GameMap;
import net.artux.pdanetwork.models.quest.StoryDto;
import net.artux.pdanetwork.models.quest.StoryInfo;
import net.artux.pdanetwork.service.quest.QuestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Tag(name = "Сюжет")
@RequestMapping("/api/v1/quest")
public class QuestController {

    private final QuestService questService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить скомпилированный сюжет по указанному ID")
    public StoryDto getStory(@PathVariable("id") long id) {
        return questService.getStory(id);
    }

    @GetMapping
    @Operation(summary = "Получить скомпилированные сюжеты")
    public Collection<StoryInfo> getStories() {
        return questService.getStoriesInfo();
    }

    @GetMapping("/{storyId}/{chapterId}")
    @Operation(summary = "Получить скомпилированную главу по указанному ID в сюжете")
    public ChapterDto getChapter(@PathVariable("storyId") long storyId, @PathVariable("chapterId") long chapterId) {
        return questService.getChapter(storyId, chapterId);
    }

    @GetMapping("/maps/{storyId}/{mapId}")
    @Operation(summary = "Получить скомпилированную карту по указанному ID в сюжете")
    public GameMap getMap(@PathVariable("storyId") long storyId, @PathVariable("mapId") long mapId) {
        return questService.getMap(storyId, mapId);
    }


}