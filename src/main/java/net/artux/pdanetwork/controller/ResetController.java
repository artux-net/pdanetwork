package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.MemberDto;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.service.member.MemberService;
import net.artux.pdanetwork.service.member.reset.ResetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;

@RestController
@RequiredArgsConstructor
@Api(tags = "Сбросы")
@RequestMapping("/reset")
public class ResetController {

  private final MemberService memberService;
  private final ResetService resetService;

  @GetMapping
  @RequestMapping("/data")
  @ApiOperation(value = "Сброс информации о прохождении")
  public MemberDto resetData(){
    return memberService.resetData();
  }

  @PutMapping
  @ApiOperation(value = "Запрос сброса пароля")
  public Status sendLetter(@RequestParam("email") String email){
    return resetService.sendLetter(email);
  }
}