package com.pdanetwork.servlets;

import com.google.gson.Gson;
import com.pdanetwork.authentication.UserManager;
import com.pdanetwork.utills.Security;
import com.pdanetwork.utills.mail.MailService;
import com.pdanetwork.models.Login.RegisterUser;
import com.pdanetwork.models.Status;
import com.pdanetwork.utills.mongo.MongoUsers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    UserManager userManager;
    Gson gson;
    MongoUsers mongoUsers;

    @Override
    public void init() {
        userManager = new UserManager();
        gson = new Gson();
        mongoUsers = new MongoUsers();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Status status;
        String loc = request.getHeader("locale");

        try {
            RegisterUser newUser = gson.fromJson(RequestReader.getString(request), RegisterUser.class);

            status = userManager.checkNewUser(newUser, loc);

            if(status.getCode()==200 & status.getSuccess()){
                String token = Security.encrypt(Integer.toString(newUser.hashCode()));
                if(new MailService().sendConfirmLetter(newUser, token)){
                    RequestDispatcher dispatcher =
                            getServletContext().getRequestDispatcher("/confirm");
                    request.setAttribute("newUser", newUser);
                    request.setAttribute("token", token);
                    try {
                        dispatcher.include(request, response);
                    } catch (ServletException | IOException e) {
                        e.printStackTrace();
                    }
                    status = new Status();
                    status.setSuccess(true);
                    status.setCode(200);
                    status.setDescription("Проверьте почту");
                }else {
                    status = new Status();
                    status.setSuccess(false);
                    status.setCode(500);
                    status.setDescription("Internal error, try later");
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            status = new Status();
            status.setSuccess(false);
            status.setDescription(ex.getMessage());
            //:TODO need more statuses
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(gson.toJson(status));
    }

    @Override
    public void destroy() {
        super.destroy();
        mongoUsers.closeClient();
        System.out.println("Register Servlet has been destroyed.");
    }
}
