package com.pdanetwork.chat;


import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.pdanetwork.chat.configurators.MessagesConfigurator;
import com.pdanetwork.models.Chat.UserMessage;
import com.pdanetwork.servlets.RequestReader;
import com.pdanetwork.utills.mongo.MongoMessages;
import com.pdanetwork.utills.mongo.MongoUsers;
import org.bson.Document;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@ServerEndpoint(value="/dialog", configurator= MessagesConfigurator.class)
public class MessagesSocket {

    private HashMap<Integer, Session> userSessions = new HashMap<>();

    MongoMessages mongoMessages = new MongoMessages();
    MongoUsers mongoUsers = new MongoUsers();
    Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session userSession) throws UnsupportedEncodingException {
        Map<String, String> params = null;
        try {
            params = RequestReader.splitQuery(userSession.getQueryString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String token = params.get("t");
        int pdaId = mongoUsers.getPdaIdByToken(token);
        int toPdaId = Integer.parseInt(params.get("toPdaId"));

        userSessions.put(pdaId,userSession);


        Block<Document> printBlock = document -> {
            try {
                userSession.getBasicRemote().sendText(document.toJson());
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        mongoMessages.getDialogCollection(mongoMessages.getDialogName(pdaId, toPdaId)).find().sort(new BasicDBObject()).limit(30).forEach(printBlock);
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
        int toPdaId = Integer.parseInt(params.get("toPdaId"));
        String dialogName = mongoMessages.getDialogName(pdaId,toPdaId);


        userSessions.remove(pdaId, userSession);
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
        int toPdaId = Integer.parseInt(params.get("toPdaId"));
        String dialogName = mongoMessages.getDialogName(pdaId,toPdaId);

        if(mongoUsers.userExists(toPdaId)) {

            if (userSessions.get(toPdaId) != null) {
                userSessions.get(toPdaId).getAsyncRemote().sendText(message);
            }

            userSession.getAsyncRemote().sendText(message);

            if (!mongoMessages.dialogExists(dialogName)) {
                mongoMessages.createDialog(dialogName);
                mongoUsers.addDialogByToken(token, toPdaId, dialogName, gson.fromJson(message, UserMessage.class).message);
                System.out.println("new dialog " + dialogName);
            } else {
                System.out.println("dialog upd");
                mongoUsers.upDialog(token, toPdaId, dialogName, gson.fromJson(message, UserMessage.class).message);
            }

            mongoMessages.getDialogCollection(dialogName).insertOne(Document.parse(message));
        } else {

            Gson gson = new Gson();
            UserMessage userMessage = new UserMessage();
            userMessage.senderLogin = "Система";
            userMessage.message = "Данного PDA ID не существует!";
            userMessage.avatarId = 30;
            userMessage.pdaId = 0;
            userMessage.groupId = 0;
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));

            userMessage.time = dateFormatGmt.format(new Date());

            userSession.getAsyncRemote().sendText(gson.toJson(userMessage));
        }
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        thr.printStackTrace();
    }

}
