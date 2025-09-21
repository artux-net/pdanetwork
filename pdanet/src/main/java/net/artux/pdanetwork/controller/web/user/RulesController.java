package net.artux.pdanetwork.controller.web.user;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Tag(name = "Правила")
@RequiredArgsConstructor
@Controller
@RequestMapping("/rules")
public class RulesController {

    @Hidden
    @GetMapping()
    public ModelAndView getRules() {
        return new ModelAndView("public/rules");
    }


}