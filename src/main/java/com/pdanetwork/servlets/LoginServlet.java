package com.pdanetwork.servlets;

import com.google.gson.Gson;
import com.pdanetwork.models.Login.LoginStatus;
import com.pdanetwork.models.Login.LoginUser;
import com.pdanetwork.models.Login.Member;
import com.pdanetwork.models.Login.UpdateData;
import com.pdanetwork.utills.mongo.MongoUsers;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    LoginStatus status;
    Gson gson = new Gson();
    MongoUsers mongo;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        mongo = new MongoUsers();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/json");
        try {
            LoginUser loginUser = gson.fromJson(RequestReader.getString(request), LoginUser.class);
            status = mongo.tryLogin(loginUser.getEmailOrLogin(), loginUser.getPassword());
        } catch (Exception ex) {
            ex.printStackTrace();
            log("Response error: ", ex);
            status.setSuccess(false);
            status.setDescription(ex.getMessage());
        }
        try {
            response.getOutputStream().print(gson.toJson(status));
            response.getOutputStream().flush();
        } catch (Exception e) {
            log("Response error: ", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("getLogin");
        String token = req.getHeader("t");
        Member member = mongo.getByToken(token);    
        resp.setContentType("application/json; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(gson.toJson(member));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token  = req.getHeader("t");
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = req.getReader().readLine()) != null) {
            sb.append(s);
        }
        UpdateData updateData = gson.fromJson(sb.toString(), UpdateData.class);
        mongo.updateMember(token, updateData);

        resp.getWriter().println("ok");

    }


    @Override
    public void destroy() {
        super.destroy();
        //TODO: make destroy void
    }
}
