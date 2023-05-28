package net.artux.pdanetwork.controller.rest.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.models.quest.admin.StoriesStatus;
import net.artux.pdanetwork.service.quest.QuestManagerService;
import net.artux.pdanetwork.service.quest.QuestService;
import net.artux.pdanetwork.utills.IsModerator;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = "Сюжет", description = "Доступен с роли модератора")
@RequestMapping("/api/v1/admin/quest")
@IsModerator
@RequiredArgsConstructor
public class AdminQuestsController {

    private final QuestService questService;
    private final QuestManagerService questManagerService;

    @Operation(summary = "Обновить сюжеты с GitHub", description = "https://github.com/artux-net/pda-quests")
    @PostMapping("/update")
    public Status updateStories() {
        return questManagerService.downloadStories();
    }

    @Operation(summary = "Получить информацию о всех сюжетах")
    @GetMapping("/status")
    public StoriesStatus getStatus() {
        return questService.getStatus();
    }

    @Operation(summary = "Загрузить zip-архив с историями")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Status uploadStories(@RequestPart("file") MultipartFile file) {
        return questManagerService.uploadStories(file);
    }

    @PostMapping(value = "/uploadCustom")
    @Operation(summary = "Загрузить пользовательский сюжет")
    public Status uploadCustomStory(@RequestBody Story story) {
        return questService.setUserStory(story);
    }

}
