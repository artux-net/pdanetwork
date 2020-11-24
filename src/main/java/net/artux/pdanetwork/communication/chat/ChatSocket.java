package net.artux.pdanetwork.communication.chat;

import com.google.gson.JsonSyntaxException;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.communication.chat.configurators.ChatSocketConfigurator;
import net.artux.pdanetwork.communication.model.LimitedArrayList;
import net.artux.pdanetwork.communication.model.UserMessage;
import net.artux.pdanetwork.utills.ServletContext;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/chat/{token}", configurator = ChatSocketConfigurator.class)
public class ChatSocket {

    private LimitedArrayList<UserMessage> lastMessages = new LimitedArrayList<>();

    @OnOpen
    public void onOpen(Session userSession, EndpointConfig config, @PathParam("token") String token) throws IOException {
        if (token.equals("*"))
            token = (String) config.getUserProperties().get("t");
        Member member = ServletContext.mongoUsers.getByToken(token);
        if (member != null) {
            userSession.getUserProperties().put("m", member);
            {
                //TODO Logic for ban
                if (!ServletContext.mongoUsers.isBlocked(token)) {
                    for (UserMessage userMessage : lastMessages)
                        userSession.getAsyncRemote().sendText(userMessage.toString());
                } else {
                    sendMessage(userSession, "Вы были заблокированы за нарушение правил.");
                    userSession.close();
                }
            }
        } else {
            userSession.close();
        }
    }

    private void sendMessage(Session userSession, String msg){
        userSession.getAsyncRemote().sendText(
                UserMessage.getSystemMessage("ru", msg).toString());
    }

    @OnClose
    public void onClose(Session userSession) {
        userSession.getAsyncRemote();
    }

    @OnMessage
    public void onMessage(String message, Session userSession) throws IllegalStateException, JsonSyntaxException {
        UserMessage userMessage = new UserMessage((Member) userSession.getUserProperties().get("m"), message);
        if(!BadWordsFilter.contains(userMessage)) {
            lastMessages.add(userMessage);
            for (Session session : userSession.getOpenSessions()) {
                session.getAsyncRemote().sendText(userMessage.toString());
            }
        } else {
            sendMessage(userSession,"Мат в общих чатах запрещен.");
        }
    }

    @OnError
    public void onError(Throwable thr) {
        ServletContext.error("Error at chat socket", thr);
        thr.printStackTrace();
    }

}