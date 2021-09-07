package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Member;
import net.artux.pdanetwork.models.MemberDto;
import net.artux.pdanetwork.authentication.register.model.RegisterUser;
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
  @GetMapping("/login")
  public MemberDto loginUser(){
    return memberService.getMemberDto();
  }

  @ApiOperation(value = "Редактирование информации")
  @PutMapping("/login")
  public Status editUser(@RequestBody RegisterUser user){
    return memberService.editMember(user);
  }

  @ApiOperation(value = "Рейтинг пользователей")
  @GetMapping("/ratings")
  public ResponsePage<UserInfo> getRating(@Valid QueryPage queryPage){
    return memberService.getRating(queryPage);
  }


  @ApiOperation(value = "Выполнение действий (надо переделывать)")
  @PutMapping("/actions")
  public Member doActions(@RequestBody HashMap<String, List<String>> actions){
    return memberService.doActions(actions);
  }

}