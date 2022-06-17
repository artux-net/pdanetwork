package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.service.member.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final MemberService memberService;

    @ApiOperation(value = "Выполнение")
    @PutMapping("/do")
    public UserEntity doActions(@RequestBody HashMap<String, List<String>> actions) {
        actionService.doUserActions(actions, memberService.getMember());
        //TODO do on server
        return memberService.getMember();
    }

}
