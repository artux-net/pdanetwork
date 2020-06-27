package net.artux.pdanetwork.servlets;


import com.google.gson.Gson;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.utills.mail.MailService;
import net.artux.pdanetwork.utills.mongo.MongoUsers;

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
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String loginOrEmail = httpServletRequest.getHeader("q");

        Member user = mongoUsers.getEmailUser(loginOrEmail);
        Status status;

        if (user != null) {
            mailService.sendOldPassword(user);

            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.getWriter().println(gson.toJson(new Status(true, "Мы отправили письмо с паролем на Вашу почту")));
        } else {
            httpServletResponse.setContentType("application/json; charset=UTF-8");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.getWriter().println(gson.toJson(new Status(false,"Данного пользователя не существует")));
        }
    }


    @Override
    public void destroy() {
        super.destroy();
        mongoUsers.close();
        System.out.println("ResetServlet has been destroyed.");
    }
}
