package net.artux.pdanetwork.service.member.reset;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import net.artux.pdanetwork.models.*;
import net.artux.pdanetwork.service.email.EmailService;
import net.artux.pdanetwork.service.member.MemberService;
import net.artux.pdanetwork.utills.Security;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ResetServiceImpl implements ResetService {

  private final MemberService memberService;
  private final EmailService emailService;

  private final MemberMapper memberMapper;

  private final HashMap<String, String> requests = new HashMap<>();

  @Override
  public Status sendLetter(String email) {
    Member member = memberService.getMemberByEmail(email);

    if (!requests.containsValue(member.getEmail())) {
      String token = Security.encrypt(member.getPassword() + member.getLogin());
      try {
          emailService.askForPassword(member, token);
          addCurrent(token, member);
          return new Status(true, "Мы отправили письмо с паролем на Вашу почту");
      } catch (Exception e) {
        return new Status(false, e.getMessage());
      }
    } else {
      return new Status(false, "Такого пользователя не существует, либо письмо уже отправлено");
    }
  }

  private void addCurrent(String token, Member user) {
    requests.put(token, user.getEmail());
    new Timer(30 * 60 * 1000, e -> requests.remove(token)).start();
  }

  @Override
  public Status changePassword(String token, String password) {
    Member member = memberService.getMemberByEmail(requests.get(token));
    RegisterUser registerUser = memberMapper.regUser(member);
    registerUser.setPassword(password);
    return memberService.editMember(registerUser);
  }

}
