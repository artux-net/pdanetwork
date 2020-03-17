package com.pdanetwork.chat;

import com.google.gson.Gson;
import com.pdanetwork.chat.configurators.GroupsSocketConfigurator;
import com.pdanetwork.models.Chat.LimitedArrayList;
import com.pdanetwork.models.Chat.UserMessage;
import com.pdanetwork.servlets.RequestReader;
import com.pdanetwork.utills.mongo.MongoUsers;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@ServerEndpoint(value="/groupChat", configurator= GroupsSocketConfigurator.class)
public class GroupsSocket {

    MongoUsers mongoUsers = new MongoUsers();
    Gson gson = new Gson();
    HashMap <Integer, List<Session>> userSessions = getUserSessions();  

    private HashMap<Integer, List<Session>> getUserSessions() {
        userSessions = new HashMap<>();

        userSessions.put(1, new ArrayList<>());
        userSessions.put(2, new ArrayList<>());
        userSessions.put(3, new ArrayList<>());
        userSessions.put(4, new ArrayList<>());
        userSessions.put(5, new ArrayList<>());
        userSessions.put(6, new ArrayList<>());

        return userSessions;
    }

    HashMap <Integer, LimitedArrayList<UserMessage>> lastMessages = getLastMessages();

    public HashMap<Integer, LimitedArrayList<UserMessage>> getLastMessages() {
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
    public void onOpen(Session userSession) throws IOException {
        Map<String, String> params = null;
        try {
            params = RequestReader.splitQuery(userSession.getQueryString());
        } catch (Exception e) {
            userSession.getAsyncRemote().sendText("You have not connected to socket");
            userSession.close();
        }

        String token = params.get("t");
        int pdaId = mongoUsers.getPdaIdByToken(token);



        if(!Objects.isNull(params.get("group"))){
            int group = Integer.parseInt(params.get("group"));
            if(group != 0){
                userSessions.get(group).add(userSession);
                userSession.getAsyncRemote().sendText(gson.toJson(lastMessages.get(group)));
            }
        } else {
            UserMessage userMessage = new UserMessage();
            userMessage.senderLogin = "Система";
            userMessage.message = "Ваш ПДА не подключен ни к одной из групп";
            userMessage.avatarId = 30;
            userMessage.pdaId = 0;
            userMessage.groupId = 0;

            userSession.getAsyncRemote().sendText(gson.toJson(userMessage));
            try {
                userSession.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(Session userSession) throws UnsupportedEncodingException {
        Map<String, String> params = null;
        try {
            params = RequestReader.splitQuery(userSession.getQueryString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String token = params.get("t");
        int pdaId = mongoUsers.getPdaIdByToken(token);
        int groupId = Integer.parseInt(params.get("group"));

        userSessions.get(groupId).remove(userSession);
    }

    @OnMessage
    public void onMessage(String message, Session userSession) throws UnsupportedEncodingException {
        Map<String, String> params = null;
        try {
            params = RequestReader.splitQuery(userSession.getQueryString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String token = params.get("t");
        int pdaId = mongoUsers.getPdaIdByToken(token);
        int group = Integer.parseInt(params.get("group"));

        UserMessage userMessage = gson.fromJson(message, UserMessage.class);
        lastMessages.get(group).add(userMessage);

        for (Session session : userSessions.get(group)){
            session.getAsyncRemote().sendText(message);
        }
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        thr.printStackTrace();
    }

}


