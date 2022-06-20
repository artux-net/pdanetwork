package net.artux.pdanetwork.service.member.reset;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.UserMapper;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.*;
import net.artux.pdanetwork.service.email.EmailService;
import net.artux.pdanetwork.service.member.UserService;
import net.artux.pdanetwork.utills.Security;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ResetServiceImpl implements ResetService {

  private final UserService userService;
  private final EmailService emailService;

  private final UserMapper userMapper;

  private final HashMap<String, String> requests = new HashMap<>();

  @Override
  public Status sendLetter(String email) {
    UserEntity userEntity = userService.getMemberByEmail(email);

    if (!requests.containsValue(userEntity.getEmail())) {
      String token = Security.encrypt(userEntity.getPassword() + userEntity.getLogin());
      try {
          emailService.askForPassword(userEntity, token);
          addCurrent(token, userEntity);
          return new Status(true, "Мы отправили письмо с паролем на Вашу почту");
      } catch (Exception e) {
        return new Status(false, e.getMessage());
      }
    } else {
      return new Status(false, "Такого пользователя не существует, либо письмо уже отправлено");
    }
  }

  private void addCurrent(String token, UserEntity user) {
    requests.put(token, user.getEmail());
    new Timer(30 * 60 * 1000, e -> requests.remove(token)).start();
  }

  @Override
  public Status changePassword(String token, String password) {
    UserEntity userEntity = userService.getMemberByEmail(requests.get(token));
    RegisterUserDto registerUser = userMapper.regUser(userEntity);
    registerUser.setPassword(password);
    return userService.editMember(registerUser);
  }

}
