package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.service.member.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;

@Controller
@Api(tags = "Аутентификация")
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {

  private final UserService userService;

  @ApiOperation(value = "Регистрация пользователя")
  @PostMapping
  @ResponseBody
  public Status registerUser(@RequestBody RegisterUserDto registerUser){
    return userService.registerUser(registerUser);
  }

  @ApiOperation(value = "Подтверждение регистрации пользователя")
  @GetMapping
  public String confirmRegistration(Model model, @QueryParam("t") String token){
    model.addAttribute("message", userService.handleConfirmation(token).getDescription());
    return "registerSuccess";
  }

}