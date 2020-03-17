package com.pdanetwork.chat;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pdanetwork.chat.configurators.ChatSocketConfigurator;
import com.pdanetwork.models.Chat.LimitedArrayList;
import com.pdanetwork.models.Chat.UserMessage;
import com.pdanetwork.servlets.RequestReader;
import com.pdanetwork.utills.mongo.MongoUsers;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@ServerEndpoint(value="/chat", configurator= ChatSocketConfigurator.class)
public class ChatSocket {

    private Set<Session> userSessions = Collections.synchronizedSet(new HashSet<Session>());

    Gson gson = new Gson();
    MongoUsers mongoUsers = new MongoUsers();

    LimitedArrayList<UserMessage> lastMessages = new LimitedArrayList<>();

    @OnOpen
    public void onOpen(Session userSession) throws IOException {
        Map<String, String> params = null;
        try {
            params = RequestReader.splitQuery(userSession.getQueryString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String token = params.get("t");
        if (!mongoUsers.isBlocked(token)){
            userSessions.add(userSession);
            userSession.getAsyncRemote().sendText(gson.toJson(lastMessages));
        } else {
            UserMessage blockedMessage = new UserMessage();
            blockedMessage.senderLogin = "Система";
            blockedMessage.message = "Вы были заблокированы за нарушение правил.";
            blockedMessage.avatarId = 30;
            blockedMessage.pdaId = 0;
            blockedMessage.groupId = 0;
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));

            blockedMessage.time = dateFormatGmt.format(new Date());
            userSession.getAsyncRemote().sendText(gson.toJson(blockedMessage));
            userSession.close();
        }
    }

    @OnClose
    public void onClose(Session userSession) {
        userSessions.remove(userSession);
    }

    @OnMessage
    public void onMessage(String message, Session userSession) throws IllegalStateException, JsonSyntaxException {
        try {
            UserMessage userMessage = gson.fromJson(message, UserMessage.class);
            lastMessages.add(userMessage);
            if(!BadWordsFilter.contains(userMessage)) {
                for (Session session : userSessions) {
                    session.getAsyncRemote().sendText(message);
                }
            } else {
                UserMessage matMessage = new UserMessage();
                matMessage.senderLogin = "Система";
                matMessage.message = "За мат вас могут покарать (анально)";
                matMessage.avatarId = 30;
                matMessage.pdaId = 0;
                matMessage.groupId = 0;
                SimpleDateFormat dateFormatGmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
                dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));

                matMessage.time = dateFormatGmt.format(new Date());

                userSession.getAsyncRemote().sendText(gson.toJson(matMessage));

            }
        } catch (JsonSyntaxException e){
            System.out.println(e.getMessage());
        }
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        thr.printStackTrace();
    }

}