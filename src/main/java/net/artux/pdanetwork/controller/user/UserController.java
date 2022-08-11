package net.artux.pdanetwork.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.models.user.dto.UserDto;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.user.reset.ResetService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Tag(name = "Пользователь")
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ResetService resetService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    @ResponseBody
    public Status registerUser(@RequestBody RegisterUserDto registerUser) {
        return userService.registerUser(registerUser);
    }

    @Operation(summary = "Подтверждение регистрации пользователя")
    @GetMapping("/register")
    public ModelAndView confirmRegistration(Model model, String t) {
        model.addAttribute("message", userService.handleConfirmation(t).getDescription());
        return new ModelAndView("registerSuccess");
    }

    @Operation(summary = "Информация о пользователе")
    @GetMapping("/info")
    public UserDto loginUser() {
        return userService.getUserDto();
    }

    @Operation(summary = "Редактирование информации")
    @PutMapping("/edit")
    public Status editUser(@RequestBody RegisterUserDto user) {
        return userService.editUser(user);
    }

    @GetMapping("/reset")
    @Operation(summary = "Сброс информации о прохождении")
    public StoryData resetData() {
        return resetService.resetData();
    }

    @PutMapping("/reset/pass")
    @Operation(summary = "Запрос сброса пароля")
    public Status sendLetter(@RequestParam("email") String email) {
        return resetService.sendLetter(email);
    }

}