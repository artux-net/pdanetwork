package com.pdanetwork.utills.mail;

import com.pdanetwork.models.Login.Member;
import com.pdanetwork.models.Login.RegisterUser;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class MailService {

    private Session session;
    private static String email = "pda@stalker-uc.ru";
    private static String password = "xxxccc111";
    private static String mailTemplateReg;
    private static String mailTemplateCon;


    public static boolean startedSuccessfully;

    static {
        try {
            //TODO:make this files
            mailTemplateReg = readFile("mail-template-reg.html");
            mailTemplateCon = readFile("mail-template-con.html");
            startedSuccessfully = true;
        } catch (IOException e) {
            System.out.println("Can not find template files.");
            System.out.println("Error message: " + e.getMessage());
            startedSuccessfully = false;
        }
    }

    public MailService(){
        // setup settings
        Properties props = new Properties();

        props.setProperty("mail.smtp.host", "smtp.mail.ru");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.auth", "true");

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });
    }

    public void sendOldPassword(Member user) throws MessagingException {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress("Сталкерский ПДА <"+email+">"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(user.getName() + " <" + user.getEmail() + ">"));
            message.setSubject("Восстановление пароля");
            message.setText(user.getLogin() +", "
                    + "\n\n Ваш пароль: " + user.getPassword());

            Transport.send(message);
    }

    public boolean sendRegisterLetter(RegisterUser user, int pdaId) throws MessagingException{
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress("Сталкерский ПДА <"+email+">"));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(user.getName() + " <" + user.getEmail() + ">"));
        message.setSubject("Регистрация");
        mailTemplateReg = mailTemplateReg.replace("login" , user.getLogin());
        mailTemplateReg = mailTemplateReg.replace("pass" , user.getPassword());
        mailTemplateReg = mailTemplateReg.replace("pdaId" , Integer.toString(pdaId));
        message.setContent(mailTemplateReg, "text/html; charset=utf-8");

        return sendMessage(message);
    }

    public boolean sendConfirmLetter(RegisterUser user, String token) throws MessagingException{
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress("Сталкерский ПДА <"+email+">"));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(user.getName() + " <" + user.getEmail() + ">"));
        message.setSubject("Подтвердите регистацию");
        mailTemplateCon = mailTemplateCon.replace("link" , "http://192.168.1.106:8080/confirm?token=" + token);
        message.setContent(mailTemplateCon, "text/html; charset=utf-8");

        return sendMessage(message);
    }

    private boolean sendMessage(Message message) {
        try {
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            System.out.println("Can not send to test letter.");
            System.out.println(e.getMessage());
            return false;
        }
    }

    static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

}
