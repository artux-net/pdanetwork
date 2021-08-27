package net.artux.pdanetwork.controller;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.repository.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

  private final MemberRepository memberRepository;

  public Status checkUser(RegisterUser user) {
    if (user.getLogin() != null && !user.getLogin().equals("")) {
      if (memberRepository.getMemberByLogin(user.getLogin()).isPresent()) {
        return new Status(false, "Пользователь с таким логином уже существует.");
      }
    } else {
      return new Status(false, "Логин не соответствует требованиям");
    }
    if (user.getEmail() != null && !user.getEmail().equals("")
            && user.getEmail().contains("@") && user.getEmail().contains(".")) {
      if (memberRepository.getMemberByEmail(user.getEmail()).isPresent()) {
        return new Status(false, "Пользователь с таким e-mail уже существует.");
      }
    } else {
      return new Status(false, "E-mail не соответствует требованиям");
    }

    if (user.getName() != null && user.getName().equals(""))
      return new Status(false, "Имя не может быть пустым");

    if (user.getNickname() != null && user.getNickname().equals(""))
      return new Status(false, "Кличка не может быть пустой");

    if (user.getPassword() != null && user.getPassword().length() < 8)
      return new Status(false, "В пароле минимум 8 символов");

    return new Status(true, "Логин и почта свободны.");
  }
}
