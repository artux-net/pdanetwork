package net.artux.pdanetwork.servlets;

import com.google.gson.Gson;
import net.artux.pdanetwork.models.FriendModel;
import net.artux.pdanetwork.utills.ServletContext;
import net.artux.pdanetwork.utills.ServletHelper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

//@WebServlet("/friends")
public class FriendsServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String token = ServletHelper.getHeaders(httpServletRequest).get("t");
        Map<String, String> query = ServletHelper.splitQuery(httpServletRequest.getQueryString());

        int pdaId = Integer.parseInt(query.get("pdaId"));
        List<FriendModel> friendModels = new ArrayList<>();

        if (query.containsKey("pdaId") && query.containsKey("type")) {
            switch (query.get("type")) {
                case "0":
                    List<Integer> list = ServletContext.mongoUsers.getFriends(pdaId);
                    for (int id : list) {
                        if (ServletContext.mongoUsers.getFriends(id).contains(pdaId))
                            friendModels.add(new FriendModel(ServletContext.mongoUsers.getProfileByPdaId(id)));
                    }
                    break;
                case "1":
                    list = ServletContext.mongoUsers.getFriendRequests(pdaId);
                    for (int id : list)
                        friendModels.add(new FriendModel(ServletContext.mongoUsers.getProfileByPdaId(id)));
                    break;
            }

            httpServletResponse.getWriter().print(gson.toJson(friendModels));
        }


    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String token = ServletHelper.getHeaders(httpServletRequest).get("t");
        Enumeration<String> names = httpServletRequest.getParameterNames();
        while(names.hasMoreElements()){
            String param = names.nextElement();
            switch (param){
                case "add":
                    int id = Integer.parseInt(httpServletRequest.getParameter(param));

                    if (ServletContext.mongoUsers.addFriend(token, id))
                        httpServletResponse.setStatus(200);
                    else
                        httpServletResponse.setStatus(500);
                    break;
                case "req":
                    id = Integer.parseInt(httpServletRequest.getParameter(param));
                    ServletContext.mongoUsers.friendRequest(token, id);
                    httpServletResponse.setStatus(200);
                    break;
                case "remove":
                    id = Integer.parseInt(httpServletRequest.getParameter(param));

                    if (ServletContext.mongoUsers.removeFriend(token, id))
                        httpServletResponse.setStatus(200);
                    else
                        httpServletResponse.setStatus(500);
                    break;
            }
        }
    }
}


