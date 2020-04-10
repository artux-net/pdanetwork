package net.artux.pdanetwork.authentication.register;

import net.artux.pdanetwork.authentication.Authorization;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private Authorization authorization;

    @Override
    public void init() {
        authorization = new Authorization();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        authorization.handleRegister(request, response);
    }

    @Override
    public void destroy() {
        super.destroy();
        System.out.println("Register Servlet has been destroyed.");
    }
}
