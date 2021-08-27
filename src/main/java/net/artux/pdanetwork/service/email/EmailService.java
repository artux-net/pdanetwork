package net.artux.pdanetwork.service.email;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.authentication.register.model.RegisterUser;

public interface EmailService {

  void askForPassword(Member user, String token);
  void sendRegisterLetter(RegisterUser user, int pdaId);
  void sendConfirmLetter(RegisterUser user, String token);

}
