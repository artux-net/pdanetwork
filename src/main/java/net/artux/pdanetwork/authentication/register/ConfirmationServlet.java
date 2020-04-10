package net.artux.pdanetwork.authentication.register;

import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import net.artux.pdanetwork.utills.mail.MailService;
import net.artux.pdanetwork.utills.mongo.MongoUsers;

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

    public static Map<String, RegisterUser> registerUserMap = new HashMap<>();
    MongoUsers mongoUsers = new MongoUsers();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(registerUserMap.containsKey(req.getParameter("token"))){
            int pdaId = mongoUsers.add(registerUserMap.get(req.getParameter("token")));
            if(mongoUsers.existsUser(registerUserMap.get(req.getParameter("token")).getLogin(), null)) {
                try {
                    new MailService().sendRegisterLetter(registerUserMap.get(req.getParameter("token")), pdaId);
                    registerUserMap.remove(req.getParameter("token"));
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                resp.setStatus(200);
                resp.getWriter().print(pdaId);
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
            System.out.println(registerUserMap.entrySet());
            registerUserMap.put((String) req.getAttribute("registerToken"), registerUser);
            new Timer(30*60*1000, e -> registerUserMap.remove(req.getAttribute("token"))).start();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        mongoUsers.close();
    }
}
