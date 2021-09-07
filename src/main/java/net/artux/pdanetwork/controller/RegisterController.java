package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.service.member.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;

@Controller
@Api(tags = "Регистрация")
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {

  private final MemberService memberService;

  @ApiOperation(value = "Регистрация пользователя")
  @PostMapping
  @ResponseBody
  public Status registerUser(@RequestBody RegisterUser registerUser){
    return memberService.registerUser(registerUser);
  }

  @ApiOperation(value = "Подтверждение регистрации пользователя")
  @GetMapping
  public String confirmRegistration(Model model, @QueryParam("t") String token){
    model.addAttribute("message", memberService.handleConfirmation(token).getDescription());
    return "registerSuccess";
  }

}