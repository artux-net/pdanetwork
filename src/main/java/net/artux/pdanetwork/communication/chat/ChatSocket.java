package net.artux.pdanetwork.communication.chat;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.artux.pdanetwork.communication.chat.configurators.ChatSocketConfigurator;
import net.artux.pdanetwork.communication.model.LimitedArrayList;
import net.artux.pdanetwork.communication.model.UserMessage;
import net.artux.pdanetwork.utills.RequestReader;
import net.artux.pdanetwork.utills.ServletContext;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;

@ServerEndpoint(value="/chat", configurator= ChatSocketConfigurator.class)
public class ChatSocket {

    private Gson gson = new Gson();

    private LimitedArrayList<UserMessage> lastMessages = new LimitedArrayList<>();

    @OnOpen
    public void onOpen(Session userSession) throws IOException {
        Map<String, String> params = RequestReader.splitQuery(userSession.getQueryString());

        String token = params.get("t");
        if (!ServletContext.mongoUsers.isBlocked(token)){
            userSession.getAsyncRemote().sendText(gson.toJson(lastMessages));
        } else {
            sendMessage(userSession,"Вы были заблокированы за нарушение правил.");
            userSession.close();
        }
    }

    private void sendMessage(Session userSession, String msg){
        userSession.getAsyncRemote().sendText(gson.toJson(
                UserMessage.getSystemMessage("ru", msg)));
    }

    @OnClose
    public void onClose(Session userSession) {
        userSession.getAsyncRemote();
    }

    @OnMessage
    public void onMessage(String message, Session userSession) throws IllegalStateException, JsonSyntaxException {
        UserMessage userMessage = gson.fromJson(message, UserMessage.class);
        if(!BadWordsFilter.contains(userMessage)) {
            lastMessages.add(userMessage);
            for (Session session : userSession.getOpenSessions()) {
                session.getAsyncRemote().sendText(message);
            }
        } else {
            sendMessage(userSession,"Мат в общих чатах запрещен.");
        }
    }

    @OnError
    public void onError(Throwable thr) {
        thr.printStackTrace();
    }

}