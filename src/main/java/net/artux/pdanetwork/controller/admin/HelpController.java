package net.artux.pdanetwork.controller.admin;

import net.artux.pdanetwork.models.user.gang.Gang;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/utility/help")
public class HelpController extends BaseUtilityController {


    public HelpController() {
        super("Обработчик команд");
    }

    @Override
    protected Object getHome(Model model) {
        model.addAttribute("gangs", Gang.values());
        return pageWithContent("help", model);
    }

}
