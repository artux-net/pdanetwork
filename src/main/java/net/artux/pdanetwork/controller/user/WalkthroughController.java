package net.artux.pdanetwork.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.CommandBlock;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.service.action.StateService;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Прохождение")
@RequestMapping("/walkthrough")
public class WalkthroughController {

    private final ActionService actionService;
    private final StateService stateService;
    private final UserService userService;

    @Operation(summary = "Выполнение действий")
    @PutMapping("/actions")
    public StoryData doActions(@RequestBody CommandBlock block) {
        return actionService.doUserActions(block.getActions(), userService.getUserById());
        //TODO do on server
    }

    @Operation(summary = "Информация о прохождении")
    @GetMapping("/info")
    public StoryData getActualData() {
        return stateService.getStoryData();
    }

}
