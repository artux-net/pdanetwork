package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.service.member.MemberService;
import net.artux.pdanetwork.service.member.reset.ResetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequiredArgsConstructor
@ApiIgnore
@RequestMapping("/reset/password")
public class PasswordController {

  private final ResetService resetService;

  @GetMapping
  public String passwordPage(Model model, @RequestParam("t") String token){
    model.addAttribute("token",token);
    return "password";
  }

  @PostMapping
  public String passwordPage(@RequestParam("token") String token, @RequestParam("password") String password){
    resetService.changePassword(token, password);
    return "passwordSuccess";
  }

}