package net.artux.pdanetwork.service.email;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.service.util.ValuesService;
import org.apache.commons.io.IOUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;

@Component
@RequiredArgsConstructor
public class EmailServiceIml implements EmailService {

  private final JavaMailSender mailSender;
  private final ValuesService valuesService;

  private static final String mailTemplateReg;
  private static final String mailTemplateCon;

  static {
      mailTemplateReg = getResource( "/mail/mail-template-reg.html");
      mailTemplateCon = getResource( "/mail/mail-template-con.html");
  }

  private static String getResource(String path){
    try {
      StringWriter writer = new StringWriter();
      IOUtils.copy(EmailServiceIml.class.getResourceAsStream(path), writer, "UTF-8");
      return writer.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return "File at path " + path + " is not found.";
    }
  }

  private void sendSimpleMessage(String to, String subject, String text) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();

    try {
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
      helper.setFrom("Сталкерский ПДА <"+valuesService.getEmail()+">");
      helper.setTo(to);
      helper.setSubject(subject);

      helper.setText(text, true);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    mailSender.send(mimeMessage);
  }

  public void askForPassword(UserEntity user, String token) {
    sendSimpleMessage(user.getEmail(), "Восстановление пароля", user.getLogin() +", "
            + "\n\n ваша ссылка для смены пароля: " +valuesService.getAddress() +"/reset/password?t=" + token
            + "\n\n Действует в течение 30 минут.");

  }

  public void sendRegisterLetter(RegisterUserDto user, Long pdaId){
    sendSimpleMessage(user.getEmail(), "Регистрация", mailTemplateReg
            .replace("${login}", user.getLogin())
            .replace("${pass}" , user.getPassword())
            .replace("${pdaId}" , String.valueOf(pdaId)));
  }

  public void sendConfirmLetter(RegisterUserDto user, String token) {
    sendSimpleMessage(user.getEmail(), "Подтвердите регистацию", mailTemplateCon
            .replace("${link}" ,  valuesService.getAddress() + "/register?token=" + token));
  }


}
