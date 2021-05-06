package net.artux.pdanetwork.communication.chat;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.communication.chat.configurators.SocketConfigurator;
import net.artux.pdanetwork.communication.model.LimitedArrayList;
import net.artux.pdanetwork.communication.model.UserMessage;
import net.artux.pdanetwork.utills.ServletContext;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@ServerEndpoint(value = "/groupChat/{token}/{group}", configurator = SocketConfigurator.class)
public class GroupsSocket {

    private HashMap <Integer, LimitedArrayList<UserMessage>> lastMessages = getLastMessages();

    private HashMap<Integer, LimitedArrayList<UserMessage>> getLastMessages() {
        lastMessages = new HashMap<>();

        int limit = 150;
        lastMessages.put(1, new LimitedArrayList<>(limit));
        lastMessages.put(2, new LimitedArrayList<>(limit));
        lastMessages.put(3, new LimitedArrayList<>(limit));
        lastMessages.put(4, new LimitedArrayList<>(limit));
        lastMessages.put(5, new LimitedArrayList<>(limit));
        lastMessages.put(6, new LimitedArrayList<>(limit));

        return lastMessages;
    }

    @OnOpen
    public void onOpen(Session userSession, EndpointConfig config,
                       @PathParam("token") String token, @PathParam("group") String gr) throws IOException {
        if (token.equals("*"))
            token = (String) config.getUserProperties().get("t");

        Member member = ServletContext.mongoUsers.getByToken(token);
        if (member != null) {
            int group = 0;
            if (gr.equals("*"))
                group = ServletContext.mongoUsers.getByToken(token).getGroup();
            else if (member.getAdmin() != 0)
                group = Integer.parseInt(gr);

            userSession.getUserProperties().put("m", member);

            if (group != 0) {
                for (UserMessage userMessage : lastMessages.get(group))
                    userSession.getAsyncRemote().sendText(userMessage.toString());
            } else {
                userSession.getAsyncRemote()
                        .sendText(UserMessage
                                .getSystemMessage("ru", "Ваш PDA не подключен ни к одной из групп").toString());
                userSession.close();
            }
        }
    }

    @OnClose
    public void onClose(Session userSession){

    }

    @OnMessage
    public void onMessage(String message, Session userSession) {
        Member member = (Member) userSession.getUserProperties().get("m");
        int group = member.getGroup();
        UserMessage userMessage = new UserMessage(member, message);
        lastMessages.get(group).add(userMessage);

        for (Session session : userSession.getOpenSessions()){
            if (((Member) session.getUserProperties().get("m")).getGroup() == group)
                session.getAsyncRemote().sendText(userMessage.toString());
        }
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        ServletContext.error("Groups socket error", thr);
    }

}


