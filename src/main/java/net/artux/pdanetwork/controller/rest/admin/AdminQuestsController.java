package net.artux.pdanetwork.controller.rest.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.quest.admin.StoriesStatus;
import net.artux.pdanetwork.service.quest.QuestService;
import net.artux.pdanetwork.utills.IsModerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Сюжет", description = "Доступен с роли модератора")
@RequestMapping("/api/v1/admin/quest")
@IsModerator
@RequiredArgsConstructor
public class AdminQuestsController {

    private final QuestService questService;

    @Operation(summary = "Обновить сюжеты с GitHub", description = "https://github.com/artux-net/pda-quests")
    @PostMapping("/update")
    public Status updateStories() {
        return questService.downloadStories();
    }

    @Operation(summary = "Получить информацию о всех сюжетах")
    @GetMapping("/status")
    public StoriesStatus getStatus() {
        return questService.getStatus();
    }

}
