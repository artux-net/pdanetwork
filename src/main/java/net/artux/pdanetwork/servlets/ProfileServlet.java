package net.artux.pdanetwork.servlets;

import com.google.gson.Gson;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.authentication.UserManager;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.utills.RequestReader;
import net.artux.pdanetwork.utills.ServletContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        Map<String, String> query_pairs = RequestReader.splitQuery(httpServletRequest.getQueryString());
        Profile profile;
        if(query_pairs.containsKey("pdaId")){
            profile = ServletContext.mongoUsers.getProfileByPdaId(httpServletRequest.getHeader("t"), Integer.parseInt(httpServletRequest.getParameter("pdaId")));
        } else {
            profile = ServletContext.mongoUsers.getByToken(httpServletRequest.getHeader("t")).getProfile();
        }

        httpServletResponse.setContentType("application/json; charset=UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().println(gson.toJson(profile));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token  = req.getHeader("t");
        HashMap actions = gson.fromJson(RequestReader.getString(req), HashMap.class);
        UserManager userManager = new UserManager();
        Member m = userManager.doUserActions(actions, token);
        if (m!=null){
            resp.getWriter().print(gson.toJson(m));
        }else {
            resp.setStatus(500);
        }
    }
}
