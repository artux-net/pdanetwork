package net.artux.pdanetwork.controller.rest.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import net.artux.pdanetwork.models.Status
import net.artux.pdanetwork.models.user.CommandBlock
import net.artux.pdanetwork.models.user.dto.RegisterUserDto
import net.artux.pdanetwork.models.user.dto.StoryData
import net.artux.pdanetwork.models.user.dto.UserDto
import net.artux.pdanetwork.service.action.ActionService
import net.artux.pdanetwork.service.user.UserService
import net.artux.pdanetwork.service.user.reset.ResetService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Пользователь")
@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
    private val resetService: ResetService,
    private val actionService: ActionService,
) {

    @Operation(summary = "Регистрация")
    @PostMapping("/register")
    fun registerUser(@RequestBody registerUser: RegisterUserDto): Status {
        return userService.registerUser(registerUser)
    }

    @Operation(summary = "Основная информация")
    @GetMapping("/info")
    fun loginUser(): UserDto {
        return userService.getUserDto()
    }

    @Operation(summary = "Редактирование основной информации")
    @PutMapping("/edit")
    fun editUser(@RequestBody user: @Valid RegisterUserDto): Status {
        return userService.editUser(user)
    }

    @PutMapping("/reset/pass")
    @Operation(summary = "Запрос на сброс пароля")
    fun sendResetPasswordLetter(@RequestParam("email") email: String): Status {
        return resetService.sendResetPasswordLetter(email)
    }

    @Operation(summary = "Выполнение действий")
    @PutMapping("/commands")
    fun applyCommands(@RequestBody block: CommandBlock): StoryData {
        return actionService.applyCommands(block.actions)
    }

    @GetMapping("/quest/info")
    @Operation(summary = "Информация о прохождении")
    fun getCurrentStoryData(): StoryData = actionService.applyCommands(emptyMap())

    @GetMapping("/quest/reset")
    @Operation(summary = "Сброс информации о прохождении")
    fun resetData(): StoryData {
        return resetService.resetData()
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Удаление аккаунта")
    fun deleteAccount(): Boolean {
        return resetService.deleteAccount()
    }
}
