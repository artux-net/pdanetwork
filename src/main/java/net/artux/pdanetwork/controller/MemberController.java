package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.models.UserDto;
import net.artux.pdanetwork.models.RegisterUser;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.ResponsePage;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.UserInfo;
import net.artux.pdanetwork.service.member.MemberService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@Api(tags = "Пользователи")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "Пользователь")
    @GetMapping("/info")
    public UserDto loginUser() {
        return memberService.getMemberDto();
    }

    @ApiOperation(value = "Редактирование информации")
    @PutMapping("/edit")
    public Status editUser(@RequestBody RegisterUser user) {
        return memberService.editMember(user);
    }


}