package net.artux.pdanetwork.communication.chat;

import com.google.gson.Gson;
import net.artux.pdanetwork.communication.chat.configurators.GroupsSocketConfigurator;
import net.artux.pdanetwork.communication.model.LimitedArrayList;
import net.artux.pdanetwork.communication.model.UserMessage;
import net.artux.pdanetwork.utills.ServletContext;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@ServerEndpoint(value="/groupChat", configurator= GroupsSocketConfigurator.class)
public class GroupsSocket {

    private Gson gson = new Gson();

    private HashMap <Integer, LimitedArrayList<UserMessage>> lastMessages = getLastMessages();

    private HashMap<Integer, LimitedArrayList<UserMessage>> getLastMessages() {
        lastMessages = new HashMap<>();

        lastMessages.put(1, new LimitedArrayList<>());
        lastMessages.put(2, new LimitedArrayList<>());
        lastMessages.put(3, new LimitedArrayList<>());
        lastMessages.put(4, new LimitedArrayList<>());
        lastMessages.put(5, new LimitedArrayList<>());
        lastMessages.put(6, new LimitedArrayList<>());

        return lastMessages;
    }

    @OnOpen
    public void onOpen(Session userSession, EndpointConfig config) throws IOException {
        String token = (String) config.getUserProperties().get("t");
        int group = ServletContext.mongoUsers.getByToken(token).getGroup();

        if(group != 0){
            userSession.getUserProperties().put("group", group);
            userSession.getAsyncRemote().sendText(gson.toJson(lastMessages.get(group)));
        } else {
            userSession.getAsyncRemote()
                    .sendText(gson.toJson(UserMessage
                            .getSystemMessage("ru","Ваш PDA не подключен ни к одной из групп")));
            userSession.close();
        }
    }

    @OnClose
    public void onClose(Session userSession){

    }

    @OnMessage
    public void onMessage(String message, Session userSession) {
        int group = (Integer) userSession.getUserProperties().get("group");
        lastMessages.get(group).add(gson.fromJson(message, UserMessage.class));

        for (Session session : userSession.getOpenSessions()){
            if ((int)session.getUserProperties().get("group")==group)
                session.getAsyncRemote().sendText(message);
        }
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        thr.printStackTrace();
    }

}


