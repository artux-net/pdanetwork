package com.pdanetwork.servlets;


import com.google.gson.Gson;
import com.pdanetwork.utills.mail.MailService;
import com.pdanetwork.models.Login.Member;
import com.pdanetwork.models.Status;
import com.pdanetwork.utills.mongo.MongoUsers;

import javax.mail.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/reset")
public class ResetServlet extends HttpServlet {

    private MongoUsers mongoUsers = new MongoUsers();
    private Gson gson = new Gson();
    private MailService mailService = new MailService();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String loginOrEmail = httpServletRequest.getHeader("q");

        Member user = mongoUsers.getEmailUser(loginOrEmail);
        Status status = new Status();

        if (user != null) {
            try {
                mailService.sendOldPassword(user);

                httpServletResponse.setContentType("application/json; charset=UTF-8");
                httpServletResponse.setCharacterEncoding("UTF-8");

                status.setSuccess(true);
                status.setCode(200);
                status.setDescription("Мы отправили письмо с паролем на Вашу почту");

                httpServletResponse.getWriter().println(gson.toJson(status));
            } catch (MessagingException e) {
                httpServletResponse.setContentType("application/json; charset=UTF-8");
                httpServletResponse.setCharacterEncoding("UTF-8");

                status.setSuccess(true);
                status.setCode(400);
                status.setDescription("Данного email не существует, попробуйте зарегистрироваться");

                httpServletResponse.getWriter().println(gson.toJson(status));
            }
        } else {
            httpServletResponse.setContentType("application/json; charset=UTF-8");
            httpServletResponse.setCharacterEncoding("UTF-8");

            status.setSuccess(true);
            status.setCode(400);
            status.setDescription("Данного пользователя не существует");

            httpServletResponse.getWriter().println(gson.toJson(status));
        }
    }


    @Override
    public void destroy() {
        super.destroy();
        mongoUsers.closeClient();
        System.out.println("ResetServlet has been destroyed.");
    }
}
