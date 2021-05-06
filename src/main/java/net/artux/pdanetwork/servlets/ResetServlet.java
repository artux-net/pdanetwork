package net.artux.pdanetwork.servlets;


import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.utills.Security;
import net.artux.pdanetwork.utills.ServletContext;
import net.artux.pdanetwork.utills.ServletHelper;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/reset")
public class ResetServlet extends HttpServlet {

    private final HashMap<String, Integer> requests = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        Map<String, String> headers = ServletHelper.getHeaders(httpServletRequest);

        if (headers.containsKey("q")) {
            String loginOrEmail = headers.get("q");
            Member user = ServletContext.mongoUsers.getEmailUser(loginOrEmail);

            httpServletResponse.setContentType("application/json;");
            httpServletResponse.setCharacterEncoding("UTF-8");
            if (user != null && !requests.containsValue(user.getPdaId())) {
                String token = Security.encrypt(user.getPassword() + user.getLogin());
                try {
                    if (ServletContext.mailService.askForPassword(user, token)) {
                        addCurrent(token, user);
                        httpServletResponse.getWriter().println(
                                new Status(true, "Мы отправили письмо с паролем на Вашу почту"));
                    } else
                        httpServletResponse.setStatus(500);
                } catch (MessagingException e) {
                    httpServletResponse.getWriter().println(new Status(false, e.getMessage()));
                }
            } else {
                httpServletResponse.getWriter().println(
                        new Status(false, "Такого пользователя не существует, либо письмо уже отправлено"));
            }
        } else if (ServletHelper.splitQuery(httpServletRequest.getQueryString()).containsKey("t")) {
            httpServletResponse.setContentType("text/html;");
            if (requests.containsKey(httpServletRequest.getParameter("t"))) {
                RequestDispatcher dispatcher = httpServletRequest.getRequestDispatcher("password.jsp");
                if (dispatcher != null) {
                    httpServletRequest.getSession().setAttribute("t", httpServletRequest.getParameter("t"));
                    dispatcher.forward(httpServletRequest, httpServletResponse);
                }
            } else {
                httpServletResponse.getWriter().print("Your token is wrong");
            }
        } else {
            httpServletResponse.getWriter().print("Where is token, hm?");
        }
    }

    private void addCurrent(String token, Member user) {
        requests.put(token, user.getPdaId());
        new Timer(30 * 60 * 1000, e -> requests.remove(token)).start();
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("text/html;");

        String pass = httpServletRequest.getParameter("password");
        String token = (String) httpServletRequest.getSession().getAttribute("t");
        if (pass != null && token != null) {
            ServletContext.mongoUsers.updatePassword(requests.get(token), pass);
            requests.remove(token);
            httpServletResponse.getWriter().print("Password was changed.");
        } else {
            httpServletResponse.getWriter().print("Password was not changed.");
        }
    }
}
