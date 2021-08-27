package net.artux.pdanetwork.communication.chat;

import com.google.gson.*;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.communication.chat.configurators.SocketConfigurator;
import net.artux.pdanetwork.communication.model.*;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.utills.ServletContext;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ServerEndpoint(value = "/dialogs/{token}")
public class DialogsSocket {

    private static LimitedArrayList<UserMessage> lastMessages = new LimitedArrayList<>(150);
    private static boolean updateMessages = false;
    private static HashMap<Integer, Session> sessions = new HashMap<>();

    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @OnOpen
    public void onOpen(Session userSession, EndpointConfig config, @PathParam("token") String token) throws IOException {
        if (token.equals("*"))
            token = (String) config.getUserProperties().get("t");
        Member member = ServletContext.mongoUsers.getByToken(token);
        if (member != null) {
            userSession.getUserProperties().put("m", member);
            sessions.put(member.getPdaId(), userSession);
            userSession.getAsyncRemote().sendText(gson.toJson(getDialogs(member)));
        } else {
            userSession.close();
        }
    }

    private void sendMessage(Session userSession, String msg){
        userSession.getAsyncRemote().sendText(
                UserMessage.getSystemMessage("ru", msg).toString());
    }

    private List<DialogResponse> getDialogs(Member member){
        List<DialogResponse> response = new ArrayList<>();
        int pda = member.getPdaId();
        for (int id : member.getDialogs()) {
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

    public void sendUpdate(int pdaId, String json){
        if (sessions.containsKey(pdaId)){
            sessions.get(pdaId).getAsyncRemote().sendText(json);
        }
    }

    public void sendError(Session session, String message){
        if (session.isOpen())
            session.getAsyncRemote().sendText(gson.toJson(new Status(false, message)));
    }

    @OnClose
    public void onClose(Session userSession) throws IOException {
        Member member = (Member) userSession.getUserProperties().get("m");
        sessions.remove(member.getPdaId());
        userSession.close();
    }

    @OnMessage
    public void onMessage(String message, Session userSession) throws IllegalStateException, JsonSyntaxException {
        Member member = (Member) userSession.getUserProperties().get("m");
        //update conversation with new title, members, owners
        ConversationRequest request = gson.fromJson(message, ConversationRequest.class);
        if (request.cid == 0){
            if (member.friends.containsAll(request.members))
                ServletContext.mongoMessages.newConversation(member.getPdaId(), request);
            else
                sendError(userSession, "Участники должны находится у вас в друзьях");
        }else{
            if (ServletContext.mongoMessages.getConversation(request.cid).owners.contains(member.getPdaId()))
                if (member.friends.containsAll(request.members))
                    ServletContext.mongoMessages.updateConversation(request);
                else
                    sendError(userSession, "Участники должны находится у вас в друзьях");
            else
                sendError(userSession, "Вы не владелец беседы");
        }

    }

    @OnError
    public void onError(Throwable thr) {
        ServletContext.error("Error at chat socket", thr);
        thr.printStackTrace();
    }

}