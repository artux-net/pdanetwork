package net.artux.pdanetwork.authentication;

import com.google.gson.Gson;
import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.utills.RequestReader;
import net.artux.pdanetwork.utills.ServletContext;
import net.artux.pdanetwork.utills.mail.MailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Authorization {

    private Gson gson = new Gson();
    private MailService mailService = new MailService();
    private static Map<String, RegisterUser> registerUserMap = new HashMap<>();

    public void handleRegister(HttpServletRequest request, HttpServletResponse response){
        String loc = request.getHeader("locale");

        try {
            RegisterUser newUser = gson.fromJson(RequestReader.getString(request), RegisterUser.class);
            Status status = ServletContext.mongoUsers.checkUser(newUser.login,
                    newUser.email);

            String token = Base64.getEncoder().encodeToString(newUser.login.getBytes());

            if(status.getSuccess()){
                System.out.println(registerUserMap.keySet());
                if (!registerUserMap.containsKey(token))
                    if(mailService.sendConfirmLetter(newUser, token)){
                        addCurrent(token, newUser);
                        status = new Status(200, "Проверьте почту", true);
                    }else {
                        status = new Status(500, "Не удалось отправить письмо, попробуйте позже", false);
                    }
                else
                    status = new Status(false, "Пользователь ожидает регистрации");
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(gson.toJson(status));
        } catch (Exception ex) {
            response.setStatus(400);
            System.out.println(ex.getMessage());
        }
    }

    private void addCurrent(String token, RegisterUser user){
        registerUserMap.put(token, user);
        new Timer(30*60*1000, e -> registerUserMap.remove(token)).start();
    }

    public void handleConfirmation(HttpServletRequest request, HttpServletResponse response){
        if(registerUserMap.containsKey(request.getParameter("t"))){
            int pdaId = ServletContext.mongoUsers.add(registerUserMap.get(request.getParameter("t")));
            try {
                mailService.sendRegisterLetter(registerUserMap.get(request.getParameter("t")), pdaId);
                registerUserMap.remove(request.getParameter("t"));
                response.getWriter().print(pdaId);
            } catch (Exception e) {
                response.setStatus(400);
                System.out.println(e.getMessage());
            }
        }else{
            response.setStatus(400);
        }
    }
}
