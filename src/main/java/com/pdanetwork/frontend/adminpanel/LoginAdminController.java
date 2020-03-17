package com.pdanetwork.frontend.adminpanel;

import com.pdanetwork.models.Login.LoginStatus;
import com.pdanetwork.models.Login.LoginUser;
import com.pdanetwork.utills.mongo.MongoUsers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "LoginAdminController", urlPatterns = "/loginAdmin")
public class LoginAdminController extends HttpServlet {

    MongoUsers mongoUsers = new MongoUsers();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LoginUser loginUser = LoginUser.fromRequestParameters(request);

        // setAttributes customer.setAsRequestAttributes(request);
        List violations = loginUser.validate();

        LoginStatus status = mongoUsers.tryAdminLogin(loginUser.getEmailOrLogin(), loginUser.getPassword());

        if(status.isSuccess()){
            request.getSession().setAttribute("token", status.getToken());
            response.sendRedirect(request.getContextPath() + "/admin");
        } else {
            String violation = status.getDescription();
            request.setAttribute("violation", violation);
            request.getRequestDispatcher("/").forward(request, response);
        }
    }

    private String determineUrl(List violations) {
        if (!violations.isEmpty()) {
            return "/";
        } else {
            return "/admin";
        }
    }

    private static class RequestCustomer {

        private final String login;
        private final String password;

        private RequestCustomer(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public static RequestCustomer fromRequestParameters(
                HttpServletRequest request) {
            return new RequestCustomer(
                    request.getParameter("login"),
                    request.getParameter("password"));
        }

        public void setAsRequestAttributes(HttpServletRequest request) {
            request.setAttribute("login", login);
            request.setAttribute("password", password);
        }

        public List validate() {
            List violations = new ArrayList<>();
            if (login.equals("")) {
                violations.add("Login является обязательным полем");
            }
            if (password.equals("")) {
                violations.add("Password является обязательным полем");
            }
            return violations;
        }

    }

    void setAttrubutes(String login, String token){

    }

}