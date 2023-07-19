package net.artux.pdanetwork.controller.rest.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.enums.EnumDto;
import net.artux.pdanetwork.models.enums.EnumMapper;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.models.user.CommandBlock;
import net.artux.pdanetwork.models.user.dto.AdminEditUserDto;
import net.artux.pdanetwork.models.user.dto.AdminUserDto;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.models.user.gang.Gang;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.service.profile.ProfileService;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.utills.security.ModeratorAccess;
import org.apache.commons.io.IOUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyMap;

@RestController
@Tag(name = "Пользователи", description = "Доступен с роли модератора")
@RequestMapping("/api/v1/admin/users")
@ModeratorAccess
@RequiredArgsConstructor
public class AdminUserController {

    private final ProfileService profileService;
    private final ActionService actionService;
    private final UserService userService;
    private final EnumMapper enumMapper;

    @GetMapping
    @Operation(summary = "Список пользователей с пагинацией и поиском по всем полям")
    public ResponsePage<SimpleUserDto> findUsers(@RequestParam(value = "query", required = false) Optional<String> query, @Valid @ParameterObject QueryPage queryPage) {
        if (query.isPresent()) {
            return profileService.findUsers(query.get(), queryPage);
        } else
            return profileService.getUsersPage(queryPage);
    }

    @PostMapping
    @Operation(summary = "Жесткий поиск по примеру")
    public ResponsePage<SimpleUserDto> findUsers(@RequestBody AdminEditUserDto example, @Valid @ParameterObject QueryPage queryPage) {
        return profileService.findUsers(example, queryPage);
    }

    @Operation(summary = "Выгрузка контактов xlsx-файлом для рассылки")
    @GetMapping("/mail/export")
    public void exportUsers(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=\"contacts.xlsx\"");

        ByteArrayInputStream stream = userService.exportMailContacts();
        IOUtils.copy(stream, response.getOutputStream());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя")
    public AdminUserDto getUser(@PathVariable UUID id) {
        return userService.getUserForAdminById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя")
    public boolean deleteUser(@PathVariable UUID id) {
        userService.deleteUserById(id);
        return true;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактирование пользователя")
    public AdminUserDto updateUser(@Valid @RequestBody AdminEditUserDto editUserDto, @PathVariable UUID id) {
        return userService.updateUser(id, editUserDto);
    }

    @PostMapping("/{id}/quest/commands")
    @Operation(summary = "Применение команд для юзера")
    public StoryData applyCommands(@PathVariable("id") UUID id, @RequestBody CommandBlock commandBlock) {
        return actionService.applyCommands(id, commandBlock.getActions());
    }

    @GetMapping("/{id}/quest/info")
    @Operation(summary = "Получение информации о прохождении юзера")
    public StoryData getCurrentStoryData(@PathVariable("id") UUID id) {
        return actionService.applyCommands(id, emptyMap());
    }

    @GetMapping("/roles")
    @Operation(summary = "Получение списка ролей")
    public EnumDto[] getRoles() {
        return enumMapper.dto(Role.values());
    }

    @GetMapping("/gangs")
    @Operation(summary = "Получение списка группировок")
    public EnumDto[] getGangs() {
        return enumMapper.dto(Gang.values());
    }
}
