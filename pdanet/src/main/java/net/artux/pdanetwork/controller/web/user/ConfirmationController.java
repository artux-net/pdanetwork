package net.artux.pdanetwork.controller.web.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Tag(name = "Подтверждение операций")
@RequiredArgsConstructor
@Controller
@CrossOrigin
@RequestMapping("/confirmation")
public class ConfirmationController {

    private final UserService userService;

    @Operation(summary = "Подтверждение регистрации пользователя")
    @GetMapping("/register")
    public ModelAndView confirmRegistration(Model model, @RequestParam String t) {
        model.addAttribute("message", userService.handleConfirmation(t).getDescription());
        return new ModelAndView("public/user/registerSuccess");
    }

}
