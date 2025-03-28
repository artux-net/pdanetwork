package net.artux.pdanetwork.controller.rest.admin.quest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.models.quest.admin.StoriesStatus;
import net.artux.pdanetwork.models.quest.map.GameMapDto;
import net.artux.pdanetwork.entity.mappers.GameMapEnumMapper;
import net.artux.pdanetwork.models.quest.map.MapEnum;
import net.artux.pdanetwork.service.quest.QuestManagerService;
import net.artux.pdanetwork.service.quest.QuestService;
import net.artux.pdanetwork.utils.security.CreatorAccess;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Tag(name = "Сюжет", description = "Доступен с роли писателя")
@RequestMapping("/api/v1/admin/quest")
@CreatorAccess
@RequiredArgsConstructor
public class AdminQuestsController {

    private final QuestService questService;
    private final QuestManagerService questManagerService;
    private final GameMapEnumMapper mapper;

    @Operation(summary = "Обновить сюжеты с GitHub", description = "https://github.com/artux-net/pda-quests")
    @PostMapping("/git/update")
    public Status readFromGit() {
        return questManagerService.readFromGit();
    }

    @Operation(summary = "Обновить сюжеты с хранилища")
    @PostMapping("/r2/update")
    public Status readFromR2() {
        return questManagerService.readFromR2();
    }

    @Operation(summary = "Получить информацию о всех публичных сюжетах")
    @GetMapping("/status")
    public StoriesStatus getStatus() {
        return questService.getStatus();
    }

    @PostMapping(value = "/set")
    @Operation(summary = "Загрузить сюжет как пользовательский")
    public Status setPrivateStory(@RequestBody Story story, @RequestParam String message) {
        return questService.setUserStory(story, message);
    }

    @PostMapping(value = "/{id}/set")
    @Operation(summary = "Установить сюжет из хранилища как пользовательский")
    public Status setPublicStory(@PathVariable("id") UUID id) {
        return questService.setUserStory(id);
    }

    @PostMapping(value = "/set/public")
    @Operation(summary = "Обновить публичный сюжет", description = "Предыдущая история станет архивной, доступен с роли ADMIN")
    public Status setPublicStory(@RequestBody Story story, @RequestParam String message) {
        return questService.setPublicStory(story, message);
    }

    @Operation(summary = "Получить информацию о картах")
    @GetMapping("/maps/all")
    public GameMapDto[] getMaps() {
        return mapper.dto(MapEnum.values());
    }

}
