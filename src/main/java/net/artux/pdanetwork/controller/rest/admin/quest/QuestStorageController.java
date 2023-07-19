package net.artux.pdanetwork.controller.rest.admin.quest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.quest.StoryType;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.models.quest.StoryBackupDto;
import net.artux.pdanetwork.service.util.QuestBackupService;
import net.artux.pdanetwork.utills.security.IsModerator;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Tag(name = "Хранилище историй", description = "Доступен с роли писателя")
@RequestMapping("/api/v1/admin/quest/storage")
@IsModerator
@RequiredArgsConstructor
public class QuestStorageController {

    private final QuestBackupService backupService;

    @PostMapping
    @Operation(summary = "Загрузить историю в хранилище")
    public StoryBackupDto uploadPublicStory(@Valid @RequestBody Story story, @Param("message") String message, @Param("type") StoryType type) {
        return backupService.saveBackup(story, type, message);
    }

    @GetMapping
    @Operation(summary = "Получить истории по типу", description = "Доступен с роли модератора")
    public ResponsePage<StoryBackupDto> getBackups(@Valid @ParameterObject QueryPage page, @Param("type") StoryType type) {
        return backupService.getBackups(type, page);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Выгрузить с хранилища историю")
    public Story getStory(@PathVariable UUID id) {
        return backupService.getBackup(id);
    }


    @GetMapping("/maps/all")
    @Operation(summary = "Получить информацию о картах")
    public StoryType[] getTypes() {
        return StoryType.values();
    }

}
