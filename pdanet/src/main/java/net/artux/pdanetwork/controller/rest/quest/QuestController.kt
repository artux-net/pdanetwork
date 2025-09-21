package net.artux.pdanetwork.controller.rest.quest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import net.artux.pdanetwork.models.quest.ChapterDto
import net.artux.pdanetwork.models.quest.GameMap
import net.artux.pdanetwork.models.quest.StoryDto
import net.artux.pdanetwork.models.quest.StoryInfo
import net.artux.pdanetwork.service.quest.QuestService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Locale

@RestController
@RequiredArgsConstructor
@Tag(name = "Сюжет")
@RequestMapping("/api/v1/quest")
class QuestController(
    private val questService: QuestService
) {

    @GetMapping("/{id}")
    @Operation(summary = "Получить скомпилированный сюжет по указанному ID")
    fun getStory(@PathVariable("id") id: Long): StoryDto {
        return questService.getStory(id)
    }

    @GetMapping
    @Operation(summary = "Сюжеты по умолчанию")
    fun getStories(locale: Locale): Collection<StoryInfo> {
        return getPublicStories(locale)
    }

    @GetMapping("/stories/public")
    @Operation(summary = "Публичные сюжеты")
    fun getPublicStories(locale: Locale): Collection<StoryInfo> {
        return questService.getPublicStories(locale)
    }

    @GetMapping("/stories/community")
    @Operation(summary = "Сюжеты сообщества")
    fun getCommunityStories(locale: Locale): Collection<StoryInfo> {
        return questService.getCommunityStories(locale)
    }

    @GetMapping("/{storyId}/{chapterId}")
    @Operation(summary = "Получить скомпилированную главу по указанному ID в сюжете")
    fun getChapter(@PathVariable("storyId") storyId: Long, @PathVariable("chapterId") chapterId: Long): ChapterDto {
        return questService.getChapter(storyId, chapterId)
    }

    @GetMapping("/maps/{storyId}/{mapId}")
    @Operation(summary = "Получить скомпилированную карту по указанному ID в сюжете")
    fun getMap(@PathVariable("storyId") storyId: Long, @PathVariable("mapId") mapId: Long): GameMap {
        return questService.getMap(storyId, mapId)
    }
}
