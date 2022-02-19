package net.artux.pdanetwork.service.email;

import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.models.RegisterUser;

public interface EmailService {

  void askForPassword(UserEntity user, String token);
  void sendRegisterLetter(RegisterUser user, int pdaId);
  void sendConfirmLetter(RegisterUser user, String token);

}
