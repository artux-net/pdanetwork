package net.artux.pdanetwork.controller.admin;

import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.service.quest.QuestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/utility/quest")
public class AdminQuestController extends BaseUtilityController {

    private final QuestService questService;

    public AdminQuestController(QuestService questService) {
        super("Сюжет");
        this.questService = questService;
    }

    @Override
    protected Object getHome(Model model) {
        List<Story> stories = questService.getStories();
        model.addAttribute("stories", stories);
        model.addAttribute("readTime", questService.getReadTime());
        return pageWithContent("quest/list", model);
    }

    @GetMapping("/update")
    public Object updateStories(Model model) {
        questService.updateStories();
        return getHome(model);
    }


}
