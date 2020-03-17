package com.pdanetwork.servlets;

import com.google.gson.Gson;
import com.pdanetwork.authentication.UserManager;
import com.pdanetwork.models.Profile;
import com.pdanetwork.utills.mongo.MongoUsers;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/user")
public class ProfileServlet extends HttpServlet {

    private MongoUsers mongoUsers;
    private Gson gson;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        mongoUsers = new MongoUsers();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        Map<String, String> query_pairs = RequestReader.splitQuery(httpServletRequest.getQueryString());
        Profile profile;
        if(query_pairs.containsKey("pdaId")){
            profile = mongoUsers.getProfileByPdaId(Integer.getInteger(query_pairs.get("pdaId")));
        } else {
            profile = mongoUsers.getByToken(httpServletRequest.getHeader("t")).getProfile();
        }

        httpServletResponse.setContentType("application/json; charset=UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().println(gson.toJson(profile));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token  = req.getHeader("t");
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = req.getReader().readLine()) != null) {
            sb.append(s);
        }
        HashMap actions = gson.fromJson(sb.toString(), HashMap.class);
        UserManager userManager = new UserManager();
        if (userManager.doUserActions(actions, token)){
            resp.getWriter().print(true);
        } else {
            resp.getWriter().print(false);
        }


    }
}
