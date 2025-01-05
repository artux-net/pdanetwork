package net.artux.pdanetwork.controller.web.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import net.artux.pdanetwork.service.user.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Tag(name = "Подтверждение операций")
@Controller
@CrossOrigin
@RequestMapping("/confirmation")
class ConfirmationController(
    private val userService: UserService
) {

    @Operation(summary = "Подтверждение регистрации пользователя")
    @GetMapping("/register")
    fun confirmRegistration(model: Model, @RequestParam t: String): ModelAndView {
        val confirmationStatus = userService.handleConfirmation(t)

        model.addAttribute("message", confirmationStatus.description)
        return ModelAndView("public/user/registerSuccess")
    }
}
