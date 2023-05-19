package net.artux.pdanetwork.controller.web.user;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.service.user.reset.ResetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Hidden
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
    return "public/user/passwordSuccess";
  }

}