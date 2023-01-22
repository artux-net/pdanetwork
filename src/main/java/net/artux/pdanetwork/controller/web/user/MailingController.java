package net.artux.pdanetwork.controller.web.user;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Tag(name = "Рассылки")
@RequiredArgsConstructor
@Controller
@RequestMapping("/mailing")
public class MailingController {

    private final UserService userService;

    @Hidden
    @Operation(summary = "Изменить статус рассылки")
    @GetMapping("/change/{id}")
    public ModelAndView confirmRegistration(Model model, @PathVariable UUID id) {
        boolean emailSetting = userService.changeEmailSetting(id);
        String message;
        if (emailSetting)
            message = "Вы успешно подписаны на рассылку.";
        else
            message = "Вы успешно отписаны от всех рассылок.";
        model.addAttribute("message", message);
        return new ModelAndView("public/user/emailSub");
    }


}