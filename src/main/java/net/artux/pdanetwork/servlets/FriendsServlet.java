package net.artux.pdanetwork.servlets;

import com.google.gson.Gson;
import net.artux.pdanetwork.models.FriendModel;
import net.artux.pdanetwork.models.Friends;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.utills.RequestReader;
import net.artux.pdanetwork.utills.ServletContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@WebServlet("/friends")
public class FriendsServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String token = RequestReader.getHeaders(httpServletRequest).get("t");
        Friends friends = new Friends();
        List<Integer> list = ServletContext.mongoUsers.getFriends(token);
        for(int id : list){
            Profile profile = ServletContext.mongoUsers.getProfileByPdaId(id);
            FriendModel friendModel = new FriendModel(profile);
            friendModel.isSub = profile.getFriends()
                    .contains(ServletContext.mongoUsers.getPdaIdByToken(token));
            friends.list.add(friendModel);
        }
        list = ServletContext.mongoUsers.getFriendRequests(token);
        for(int id : list){
            Profile profile = ServletContext.mongoUsers.getProfileByPdaId(id);
            FriendModel friendModel = new FriendModel(profile);
            friends.list.add(friendModel);
        }
        httpServletResponse.getWriter().print(gson.toJson(friends));

    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String token = RequestReader.getHeaders(httpServletRequest).get("t");
        Enumeration<String> names = getInitParameterNames();
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


