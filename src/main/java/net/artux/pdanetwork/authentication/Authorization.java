package net.artux.pdanetwork.authentication;

import com.google.gson.Gson;
import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.utills.Security;
import net.artux.pdanetwork.utills.ServletContext;
import net.artux.pdanetwork.utills.ServletHelper;
import net.artux.pdanetwork.utills.mail.MailService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Authorization {
    private final Gson gson = new Gson();
    private final MailService mailService = new MailService();
    private static final Map<String, RegisterUser> registerUserMap = new HashMap<>();

    public void handleRegister(HttpServletRequest request, HttpServletResponse response) {
        //String loc = request.getHeader("locale");
        try {
            RegisterUser newUser = gson.fromJson(ServletHelper.getString(request), RegisterUser.class);
            ServletContext.log("Try to register new user: " + newUser.email + ", login" + newUser.login);
            Status status = ServletContext.mongoUsers.checkUser(newUser);

            String token = Security.encrypt(newUser.login);

            if (status.getSuccess()) {
                if (!registerUserMap.containsKey(token))
                    try {
                        if (mailService.sendConfirmLetter(newUser, token)) {
                            addCurrent(token, newUser);
                            status = new Status(200, "Проверьте почту.", true);
                        } else {
                            status = new Status(500, "Не удалось отправить письмо, попробуйте позже.", false);
                        }
                    } catch (MessagingException e) {
                        status = new Status(500, "Не удалось отправить письмо на " + newUser.email, false);
                    }
                else
                    status = new Status(false, "Пользователь ожидает регистрации, проверьте почту.");
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(status);
            ServletContext.log(newUser.email + " : " + status.toString());
        } catch (Exception ex) {
            response.setStatus(400);
            ServletContext.error("AuthError", ex);
        }
    }

    private void addCurrent(String token, RegisterUser user){
        ServletContext.log("Add to register wait list with token: " + token + ", " + user.email);
        registerUserMap.put(token, user);
        new Timer(30*60*1000, e -> registerUserMap.remove(token)).start();
    }

    public void handleConfirmation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (registerUserMap.containsKey(request.getParameter("t"))) {
            int pdaId = ServletContext.mongoUsers.add(registerUserMap.get(request.getParameter("t")));
            ServletContext.log("Registered user" + registerUserMap.get(request.getParameter("t")).email + " with pdaId:" + pdaId);
            registerUserMap.remove(request.getParameter("t"));
            try {
                mailService.sendRegisterLetter(registerUserMap.get(request.getParameter("t")), pdaId);
            } catch (Exception e) {
                response.setStatus(400);
                ServletContext.error("Confirmation Error", e);
            } finally {
                response.getWriter().println(pdaId);
            }
        }else{
            response.setStatus(400);
            ServletContext.log("Wrong token for confirmation " + request.getParameter("t"));
        }
    }
}
