package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.service.member.UserService;
import net.artux.pdanetwork.service.member.reset.ResetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "Сбросы")
@RequestMapping("/reset")
public class ResetController {

    private final UserService userService;
    private final ResetService resetService;

    @GetMapping
    @RequestMapping("/data")
    @ApiOperation(value = "Сброс информации о прохождении")
    public StoryData resetData() {
        return resetService.resetData();
    }

    @PutMapping
    @ApiOperation(value = "Запрос сброса пароля")
    public Status sendLetter(@RequestParam("email") String email) {
        return resetService.sendLetter(email);
    }
}