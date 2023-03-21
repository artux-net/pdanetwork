package net.artux.pdanetwork.controller.web.admin;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.service.quest.QuestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utility/triggers")
@RequiredArgsConstructor
public class StoryUpdateTriggerController {

    private final QuestService questService;

    @PostMapping("/stories/update")
    protected Status updateStories() {
        return questService.downloadStories();
    }

}
