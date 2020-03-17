package com.pdanetwork.servlets;

import com.pdanetwork.authentication.UserManager;
import com.pdanetwork.models.Login.RegisterUser;
import com.pdanetwork.utills.mongo.MongoUsers;

import javax.mail.MessagingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/confirm")
public class ConfirmationServlet extends HttpServlet {

    private Map<String, RegisterUser> registerUserMap = new HashMap<>();
    MongoUsers mongoUsers = new MongoUsers();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println(req.getRemoteAddr());
        if(registerUserMap.containsKey(req.getParameter("token"))){
            int pdaId = mongoUsers.add(registerUserMap.get(req.getParameter("token")));
            if(mongoUsers.existsUserBool(registerUserMap.get(req.getParameter("token")).getLogin(), null)) {
                try {
                    UserManager.getMailService().sendRegisterLetter(registerUserMap.get(req.getParameter("token")), pdaId);
                    resp.setStatus(200);
                    resp.getWriter().print(pdaId);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }else{
                resp.setStatus(400);
                resp.getWriter().print("Internal error");
            }
        }else{
                resp.getWriter().print("Bad request");
                resp.setStatus(400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(req.getRemoteAddr().equals("0:0:0:0:0:0:0:1")) {
            RegisterUser registerUser = (RegisterUser) req.getAttribute("newUser");
            registerUserMap.put((String) req.getAttribute("token"), registerUser);
            new Timer(30*60*1000, e -> registerUserMap.remove(req.getAttribute("token"))).start();
        } else {
            resp.setStatus(400);
            resp.getWriter().print("Bad request");
        }
    }
}
