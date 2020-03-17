package com.pdanetwork.models.Login;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class LoginUser {

    private String emailOrLogin;
    private String password;

    public LoginUser(String emailOrLogin, String password) {
        this.emailOrLogin = emailOrLogin;
        this.password = password;
    }

    public String getEmailOrLogin() {
        return emailOrLogin;
    }

    public String getPassword() {
        return password;
    }

    public static LoginUser fromRequestParameters(
            HttpServletRequest request) {
        return new LoginUser(
                request.getParameter("login"),
                request.getParameter("password"));
    }


    public List validate() {
        List violations = new ArrayList<>();
        if (emailOrLogin.equals("")) {
            violations.add("Login является обязательным полем");
        }
        if (password.equals("")) {
            violations.add("Password является обязательным полем");
        }
        return violations;
    }
}
