package net.artux.pdanetwork.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.artux.pdanetwork.configuration.handlers.ChatHandler;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.service.items.ItemService;
import net.artux.pdanetwork.service.profile.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@Tag(name = "Панель администратора")
@RequestMapping("/utility")
public class AdminController extends BaseUtilityController {

    private final ChatHandler chatHandler;
    private final ProfileService profileService;

    private static Date readTime = new Date();

    public AdminController(ChatHandler chatHandler, ProfileService profileService) {
        super("Админ панель");
        this.chatHandler = chatHandler;
        this.profileService = profileService;
    }

    @Override
    protected Object getHome(Model model) {
        model.addAttribute("rating", profileService.getRating(new QueryPage()));
        return pageWithContent("admin", model);
    }

    @GetMapping("/settings")
    public String getSettings(Model model) {
        model.addAttribute("readTime", readTime);
        return pageWithContent("notReady", model);
    }

    @GetMapping("/chat")
    public String getChatPage(Model model) {
        return pageWithContent("notReady", model);
    }

    @PostMapping("/chat/ban/{id}")
    public String banUser(Model model, @PathVariable("id") Long pdaId, String reason) {
        chatHandler.addToBanList(pdaId, reason);
        return getChatPage(model);
    }

    @PostMapping("/chat/delete/{time}")
    public String deleteMessage(Model model, @PathVariable("time") Long time) {
        chatHandler.removeMessage(time);
        return getChatPage(model);
    }

    @GetMapping("/users")
    public String getUsersPage(Model model) {
        return pageWithContent("notReady", model);
    }


}
