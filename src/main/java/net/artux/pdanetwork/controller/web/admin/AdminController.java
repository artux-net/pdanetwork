package net.artux.pdanetwork.controller.web.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.artux.pdanetwork.configuration.handlers.ChatHandler;
import net.artux.pdanetwork.service.profile.ProfileService;
import net.artux.pdanetwork.service.statistic.StatisticService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;

@Controller
@Tag(name = "Панель администратора")
@RequestMapping("/utility")
public class AdminController extends BaseUtilityController {

    private final ChatHandler chatHandler;
    private final ProfileService profileService;
    private final StatisticService statisticService;

    public AdminController(ChatHandler chatHandler, ProfileService profileService, StatisticService statisticService) {
        super("Админ панель");
        this.chatHandler = chatHandler;
        this.profileService = profileService;
        this.statisticService = statisticService;
    }

    @Override
    protected Object getHome(Model model) {
        model.addAttribute("userPage", profileService.getPage(PageRequest.of(0, 20, Sort.by("xp"))));
        model.addAttribute("totalRegistrations", statisticService.countUsers());
        model.addAttribute("todayRegistrations", statisticService.countRegistrationsToday());
        model.addAttribute("nowOnline", statisticService.countOnlineNow());
        model.addAttribute("todayOnline", statisticService.countOnlineToday());
        model.addAttribute("serverTime", Instant.now());

        return pageWithContent("admin", model);
    }

    @GetMapping("/settings")
    public String getSettings(Model model) {
        return pageWithContent("notReady", model);
    }

    @GetMapping("/chat")
    public String getChatPage(Model model) {
        return pageWithContent("chat", model);
    }

}
