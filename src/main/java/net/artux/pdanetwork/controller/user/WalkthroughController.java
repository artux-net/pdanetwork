package net.artux.pdanetwork.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.CommandBlock;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.service.action.ActionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Прохождение")
@RequestMapping("/walkthrough")
public class WalkthroughController {

    private final ActionService actionService;

    @Operation(summary = "Выполнение действий")
    @PutMapping("/actions")
    public StoryData doActions(@RequestBody CommandBlock block) {
        return actionService.doUserActions(block.getActions());
    }

    @Operation(summary = "Информация о прохождении")
    @GetMapping("/info")
    public StoryData getActualData() {
        return actionService.doUserActions(null);
    }

}
