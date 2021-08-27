package net.artux.pdanetwork.communication.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.communication.chat.configurators.SocketConfigurator;
import net.artux.pdanetwork.communication.model.LimitedArrayList;
import net.artux.pdanetwork.communication.model.UserMessage;
import net.artux.pdanetwork.utills.ServletContext;

import javax.swing.*;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

@ServerEndpoint(value = "/chat/{token}")
public class ChatSocket {

    private static LimitedArrayList<UserMessage> lastMessages = new LimitedArrayList<>(150);
    private static boolean updateMessages = false;
    private static HashMap<Integer, String> banMap = new HashMap<>();

    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @OnOpen
    public void onOpen(Session userSession, EndpointConfig config, @PathParam("token") String token) throws IOException {
        if (token.equals("*"))
            token = (String) config.getUserProperties().get("t");
        Member member = ServletContext.mongoUsers.getByToken(token);
        if (member != null) {
            userSession.getUserProperties().put("m", member);
            {
                if (!ServletContext.mongoUsers.isBlocked(token)) {
                    userSession.getAsyncRemote().sendText(gson.toJson(lastMessages));
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
    public void onClose(Session userSession) throws IOException {
        userSession.close();
    }

    @OnMessage
    public void onMessage(String message, Session userSession) throws IllegalStateException, JsonSyntaxException {
        UserMessage userMessage = new UserMessage((Member) userSession.getUserProperties().get("m"), message);
        if(!BadWordsFilter.contains(userMessage)) {
            if (!banMap.containsKey(userMessage.pdaId)){
                lastMessages.add(userMessage);
                for (Session session : userSession.getOpenSessions()) {
                    if (updateMessages) {
                        userSession.getAsyncRemote().sendText(gson.toJson(lastMessages));
                        updateMessages = false;
                    } else session.getAsyncRemote().sendText(gson.toJson(userMessage));
                }
            }else{
                sendMessage(userSession, "Вы заблокированы на час.");
            }
        } else {
            sendMessage(userSession, "Мат в общих чатах запрещен.");
            ServletContext.log("Bad word detected, pdaId: " + userMessage.pdaId + ", message: " + userMessage.message);
        }
    }

    @OnError
    public void onError(Throwable thr) {
        ServletContext.error("Error at chat socket", thr);
        thr.printStackTrace();
    }

    public static void addToBanList(Integer pdaId, String reason){
        banMap.put(pdaId, reason);
        ServletContext.log("pdaId: " + pdaId + " banned for hour in general chat: " + reason);
        new Timer(60*60*1000, e -> banMap.remove(pdaId)).start();
    }

    public static void removeMessage(Long time){
        lastMessages.removeIf(userMessage -> {
            if (userMessage.time == time)
                return true;
            else
                return false;
        });
        updateMessages = true;
    }


}