package net.artux.pdanetwork.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.communication.model.Conversation;
import net.artux.pdanetwork.communication.model.DialogResponse;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.utills.RequestReader;
import net.artux.pdanetwork.utills.ServletContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/dialogs")
public class DialogsServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getHeader("t");
        Member member = ServletContext.mongoUsers.getByToken(token);
        resp.setContentType("application/json; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(gson.toJson(getResponseList(member.getDialogs(), token)));
    }

    private List<DialogResponse> getResponseList(List<Integer> dialogs, String token){
        List<DialogResponse> response = new ArrayList<>();
        int pda = ServletContext.mongoUsers.getPdaIdByToken(token);
        for (int id : dialogs){
            Conversation conversation = ServletContext.mongoMessages.getConversation(id);

            System.out.println(gson.toJson(conversation));

            if(conversation.getMembers().size()<=2){
                int anotherId = getAnotherId(conversation.getMembers(), pda);
                System.out.println(anotherId);
                Profile profile = ServletContext.mongoUsers.getProfileByPdaId(anotherId);
                response.add(new DialogResponse(conversation, profile));
                System.out.println(111);
            } else {
                response.add(new DialogResponse(conversation));
                System.out.println(222);
            }
        }
        return response;
    }

    private int getAnotherId(List<Integer> ids, int pda){
        for (int i : ids){
            if(i!=pda){
                return i;
            }
        }
        return 0;
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String token = httpServletRequest.getHeader("t");
        Member member = ServletContext.mongoUsers.getByToken(token);
        Type type = new TypeToken<List<Integer>>(){}.getType();
        List<Integer> members = new Gson().fromJson(RequestReader.getString(httpServletRequest), type);
        ServletContext.mongoMessages.newConversation(member.getPdaId(), members);
        httpServletResponse.setContentType("application/json; charset=UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().print(gson.toJson(new Status(true, "Added")));
    }
}
