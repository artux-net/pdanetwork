package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.service.action.StateService;
import net.artux.pdanetwork.service.member.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "Обработчик команд")
@RequestMapping("/commands")
public class CommandController {

    private final ActionService actionService;
    private final StateService stateService;
    private final UserService userService;

    @ApiOperation(value = "Выполнение")
    @PutMapping("/do")
    public StoryData doActions(@RequestBody HashMap<String, List<String>> actions) {
        return actionService.doUserActions(actions, userService.getMember());
        //TODO do on server
    }

    @ApiOperation(value = "Информация о прохождении")
    @GetMapping
    public StoryData getActualData() {
        return stateService.getStoryData(userService.getMember());
    }

}
