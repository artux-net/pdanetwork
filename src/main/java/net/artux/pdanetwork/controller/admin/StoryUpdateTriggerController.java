package net.artux.pdanetwork.controller.admin;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.service.quest.QuestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utility/triggers")
@RequiredArgsConstructor
public class StoryUpdateTriggerController {

    private final QuestService questService;

    @RequestMapping(path = "/stories/update", method = RequestMethod.POST)
    protected void updateStories() {
        questService.updateStories();
    }

}
