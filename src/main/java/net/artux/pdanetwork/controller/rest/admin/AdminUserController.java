package net.artux.pdanetwork.controller.rest.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.user.CommandBlock;
import net.artux.pdanetwork.models.user.dto.AdminEditUserDto;
import net.artux.pdanetwork.models.user.dto.AdminUserDto;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.service.profile.ProfileService;
import net.artux.pdanetwork.service.user.UserService;
import org.apache.commons.io.IOUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@Tag(name = "Пользователи - для администратора", description = "Доступен с роли модератора")
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasAuthority('MODERATOR')")
@RequiredArgsConstructor
public class AdminUserController {

    private final ProfileService profileService;
    private final ActionService actionService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Получение списка пользователей с пагинацией и поиском")
    public Page<SimpleUserDto> listUsers(@RequestParam("login") Optional<String> login, @Valid @ParameterObject QueryPage queryPage) {
        if (login.isPresent()) {
            return profileService.getUsersPageByLoginContaining(login.get(), queryPage);
        } else
            return profileService.getUsersPage(queryPage);
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
    public AdminUserDto updateUser(@Valid AdminEditUserDto editUserDto, @PathVariable UUID id) {
        return userService.updateUser(id, editUserDto);
    }

    @PostMapping("/commands/apply/{id}")
    @Operation(summary = "Применение команд")
    public StoryData applyCommands(@PathVariable("id") UUID id, CommandBlock commandBlock) {
        return actionService.doUserActions(id, commandBlock.getActions());
    }

    @GetMapping("/roles")
    @Operation(summary = "Получение списка ролей")
    public Role[] getRoles() {
        return Role.values();
    }
}
