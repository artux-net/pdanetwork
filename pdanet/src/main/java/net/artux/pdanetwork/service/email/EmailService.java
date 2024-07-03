package net.artux.pdanetwork.service.email;

import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;

public interface EmailService {

  void askForPassword(UserEntity user, String token);
  void sendRegisterLetter(UserEntity user);
  void sendConfirmLetter(RegisterUserDto user, String token);

}
