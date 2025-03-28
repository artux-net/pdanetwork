package net.artux.pdanetwork.controller.rest.admin.quest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.quest.StoryType;
import net.artux.pdanetwork.dto.page.QueryPage;
import net.artux.pdanetwork.dto.page.ResponsePage;
import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.models.quest.StoryBackupDto;
import net.artux.pdanetwork.models.quest.StoryBackupEditDto;
import net.artux.pdanetwork.service.quest.QuestBackupService;
import net.artux.pdanetwork.utils.security.CreatorAccess;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Tag(name = "Хранилище историй", description = "Актуальными истории считаются те, которые публичны и не являются архивными")
@RequestMapping("/api/v1/admin/quest/storage")
@CreatorAccess
@RequiredArgsConstructor
public class QuestStorageController {

    private final QuestBackupService backupService;

    @PostMapping("/upload")
    @Operation(summary = "Загрузить историю в хранилище", description = "Предыдущая с этим id станет архивной")
    public StoryBackupDto uploadPublicStory(@Valid @RequestBody Story story, @RequestParam("message") String message) {
        return backupService.saveStory(story, message);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить все истории по типу", description = "Может только модератор, надо в админку")
    public ResponsePage<StoryBackupDto> getAllBackups(@Valid @ParameterObject QueryPage page,
                                                      @RequestParam(value = "storyId", required = false) Long storyId,
                                                      @RequestParam(value = "type", defaultValue = "PUBLIC") StoryType type,
                                                      @RequestParam(value = "archive", defaultValue = "false") boolean archive) {
        return backupService.getAllBackups(storyId, type, archive, page);
    }

    @GetMapping
    @Operation(summary = "Истории юзера")
    public ResponsePage<StoryBackupDto> getMyBackups(@Valid @ParameterObject QueryPage page,
                                                     @RequestParam(value = "storyId", required = false) Long storyId,
                                                     @RequestParam(value = "archive", required = false, defaultValue = "false") boolean archive) {
        return backupService.getUserBackups(storyId, page, archive);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Выгрузить с хранилища историю", description = "Модератор может выгрузить любую, писатель только свои")
    public Story getStory(@PathVariable UUID id) {
        return backupService.getBackup(id);
    }

    @PutMapping(value = "/{id}/type")
    @Operation(summary = "Изменить тип, админ может публиковать, писатель только в сообщество, юзер никак")
    public StoryBackupDto updateType(@PathVariable UUID id, @RequestParam StoryType type) {
        return backupService.changeType(id, type);
    }

    @PutMapping(value = "/{id}/archive")
    @Operation(summary = "Изменить архивность")
    public StoryBackupDto makeArchive(@PathVariable UUID id, @RequestParam boolean archive) {
        return backupService.makeArchive(id, archive);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Обновить информацию о истории в хранилище", description = "Модератор может обновить любую, юзер только свои")
    public StoryBackupDto updateStory(@PathVariable UUID id, @RequestBody @Valid StoryBackupEditDto dto) {
        return backupService.update(id, dto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удалить историю", description = "Модератор может удалить любую, писатель только свои")
    public boolean deleteStory(@PathVariable UUID id) {
        return backupService.deleteBackup(id);
    }

    @GetMapping("/types")
    @Operation(summary = "Типы историй в хранилище")
    public StoryType[] getTypes() {
        return StoryType.values();
    }

}
