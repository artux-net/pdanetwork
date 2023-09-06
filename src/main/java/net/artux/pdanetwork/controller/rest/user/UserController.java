package net.artux.pdanetwork.controller.rest.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.CommandBlock;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.models.user.dto.UserDto;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.user.reset.ResetService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Пользователь")
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final ResetService resetService;
    private final ActionService actionService;

    @Operation(summary = "Регистрация")
    @PostMapping("/register")
    public Status registerUser(@RequestBody RegisterUserDto registerUser) {
        return userService.registerUser(registerUser);
    }

    @Operation(summary = "Основная информация")
    @GetMapping("/info")
    public UserDto loginUser() {
        return userService.getUserDto();
    }

    @Operation(summary = "Редактирование основной информации")
    @PutMapping("/edit")
    public Status editUser(@Valid @RequestBody RegisterUserDto user) {
        return userService.editUser(user);
    }

    @PutMapping("/reset/pass")
    @Operation(summary = "Запрос на сброс пароля")
    public Status sendResetPasswordLetter(@RequestParam("email") String email) {
        return resetService.sendResetPasswordLetter(email);
    }

    @Operation(summary = "Выполнение действий")
    @PutMapping("/commands")
    public StoryData applyCommands(@RequestBody CommandBlock block) {
        return actionService.applyCommands(block.getActions());
    }

    @Operation(summary = "Информация о прохождении")
    @GetMapping("/quest/info")
    public StoryData getCurrentStoryData() {
        return actionService.applyCommands(null);
    }

    @GetMapping("/quest/reset")
    @Operation(summary = "Сброс информации о прохождении")
    public StoryData resetData() {
        return resetService.resetData();
    }

}