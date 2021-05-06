package net.artux.pdanetwork.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.communication.model.Conversation;
import net.artux.pdanetwork.communication.model.DialogResponse;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.utills.ServletContext;
import net.artux.pdanetwork.utills.ServletHelper;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@WebServlet(asyncSupported = true, value = "/dialogs")
public class DialogsServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getHeader("t");
        Member member = ServletContext.mongoUsers.getByToken(token);

        if (member != null) {
            if (!ServletHelper.getHeaders(req).containsKey("f")) {
                AsyncContext context = req.startAsync();
                context.setTimeout(12000);
                Waiter waiter = new Waiter(context);
                context.addListener(new AsyncListener() {
                    @Override
                    public void onComplete(AsyncEvent asyncEvent) throws IOException {
                        asyncEvent.getAsyncContext().getResponse().setContentType("application/json; charset=UTF-8");
                        asyncEvent.getAsyncContext().getResponse().setCharacterEncoding("UTF-8");
                        asyncEvent.getAsyncContext().getResponse().getWriter().println(gson.toJson(getResponseList(member.getDialogs(), token)));
                    }

                    @Override
                    public void onTimeout(AsyncEvent asyncEvent) throws IOException {
                        ServletContext.mongoUsers.removeFromWaitList(member);
                    }

                    @Override
                    public void onError(AsyncEvent asyncEvent) throws IOException {
                        ServletContext.error("Dialogs", asyncEvent.getThrowable());
                    }

                    @Override
                    public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
                        ServletContext.mongoUsers.addToWaitList(member, waiter);
                    }
                });
                context.start(waiter);
            } else {
                resp.setContentType("application/json; charset=UTF-8");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().println(gson.toJson(getResponseList(member.getDialogs(), token)));
            }

        } else
            resp.sendError(400);

    }

    private List<DialogResponse> getResponseList(List<Integer> dialogs, String token){
        List<DialogResponse> response = new ArrayList<>();
        int pda = ServletContext.mongoUsers.getPdaIdByToken(token);
        for (int id : dialogs) {
            Conversation conversation = ServletContext.mongoMessages.getConversation(id);

            if (conversation.allMembers().size() <= 2) {
                int anotherId = getAnotherId(conversation.allMembers(), pda);
                Profile profile = ServletContext.mongoUsers.getProfileByPdaId(anotherId);
                response.add(new DialogResponse(conversation, profile));
            } else {
                response.add(new DialogResponse(conversation));
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
        if (member != null) {
            Type type = new TypeToken<List<Integer>>() {
            }.getType();
            List<Integer> members = gson.fromJson(ServletHelper.getString(httpServletRequest), type);
            ServletContext.mongoMessages.newConversation(member.getPdaId(), members);
            httpServletResponse.setContentType("application/json; charset=UTF-8");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.getWriter().print(gson.toJson(new Status(true, "Added")));
        } else
            httpServletResponse.sendError(400);
    }
}
