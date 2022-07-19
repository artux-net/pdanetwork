package net.artux.pdanetwork.controller.admin;

import net.artux.pdanetwork.configuration.handlers.ChatHandler;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.service.files.ItemProvider;
import net.artux.pdanetwork.service.files.SellerManager;
import net.artux.pdanetwork.service.profile.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.ws.rs.QueryParam;
import java.util.Date;

@Controller
@ApiIgnore
@RequestMapping("/utility")
public class AdminController extends BaseUtilityController {

    private final ChatHandler chatHandler;
    private final ProfileService profileService;
    private final ItemProvider itemProvider;
    private final SellerManager sellerManager;
    private static Date readTime = new Date();

    public AdminController(ChatHandler chatHandler, ProfileService profileService, ItemProvider itemProvider, SellerManager sellerManager) {
        super("Админ панель");
        this.chatHandler = chatHandler;
        this.itemProvider = itemProvider;
        this.sellerManager = sellerManager;
        this.profileService = profileService;
    }

    @Override
    protected Object getHome(Model model) {
        model.addAttribute("rating", profileService.getRating(new QueryPage()));
        return pageWithContent("admin", model);
    }

    @PostMapping("/reset")
    public String reset(Model model) {
        readTime = new Date();
        itemProvider.reset();
        sellerManager.reset();

        return getSettings(model);
    }

    @GetMapping("/settings")
    public String getSettings(Model model) {
        model.addAttribute("readTime", readTime);
        return pageWithContent("settings", model);
    }

    @GetMapping("/chat")
    public String getChatPage(Model model) {
        return pageWithContent("chat", model);
    }

    @PostMapping("/chat/ban/{id}")
    public String banUser(Model model, @PathVariable("id") Long pdaId, @QueryParam("comment") String reason) {
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
        return pageWithContent("users", model);
    }


}
