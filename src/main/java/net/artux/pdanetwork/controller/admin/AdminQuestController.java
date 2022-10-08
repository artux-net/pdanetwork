package net.artux.pdanetwork.controller.admin;

import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.service.quest.QuestService;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/utility/quest")
public class AdminQuestController extends BaseUtilityController {

    private final QuestService questService;
    private final ValuesService valuesService;

    public AdminQuestController(QuestService questService, ValuesService valuesService) {
        super("Сюжет");
        this.questService = questService;
        this.valuesService = valuesService;
    }

    @Override
    protected Object getHome(Model model) {
        List<Story> stories = questService.getStories();
        model.addAttribute("stories", stories);
        long minute = Instant.now()
                .minus(questService.getReadTime().toEpochMilli(), ChronoUnit.MILLIS)
                .toEpochMilli() / (1000 * 60);
        model.addAttribute("readTime", minute);
        return pageWithContent("quest/list", model);
    }

    @GetMapping("/update")
    public Object updateStories(Model model) {
        questService.updateStories();
        return getHome(model);
    }

    @ModelAttribute("fileServer")
    public String getFileServer() {
        return valuesService.getFilesAddress();
    }

}
