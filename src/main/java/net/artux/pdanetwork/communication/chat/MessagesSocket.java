package net.artux.pdanetwork.communication.chat;


import net.artux.pdanetwork.communication.chat.configurators.MessagesConfigurator;
import net.artux.pdanetwork.communication.utilities.MongoMessages;
import net.artux.pdanetwork.utills.RequestReader;
import net.artux.pdanetwork.utills.ServletContext;
import org.bson.Document;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerEndpoint(value="/dialog", configurator= MessagesConfigurator.class)
public class MessagesSocket {

    private HashMap<Integer, List<Session>> conversations = new HashMap<>();

    private MongoMessages mongoMessages = new MongoMessages();

    @OnOpen
    public void onOpen(Session userSession, EndpointConfig config) throws IOException {
        System.out.println("Opened session: " + userSession.getId());
        Map<String, String> params = RequestReader.splitQuery(userSession.getQueryString());

        String token = (String) config.getUserProperties().get("t");
        int pdaId = ServletContext.mongoUsers.getPdaIdByToken(token);

        userSession.getUserProperties().put("t", token);
        userSession.getUserProperties().put("pda", pdaId);
        if (params.containsKey("to")) {
            int id = Integer.parseInt(params.get("to"));
            int conversationId = mongoMessages.getDialogID(pdaId, id);
            if (conversationId != 0) {
                addToConversation(conversationId, userSession);
            } else {
                userSession.getUserProperties().put("first", true);
                userSession.getUserProperties().put("pda", pdaId);
                userSession.getUserProperties().put("to", id);
            }
        } else if (params.containsKey("c")) {
            int conversationId = Integer.parseInt(params.get("c"));
            if (mongoMessages.hasConversation(conversationId)
                    && mongoMessages.conversationHas(conversationId, pdaId)) {
                addToConversation(conversationId, userSession);
            } else {
                CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "Wrong req");
                userSession.close(closeReason);
            }
        } else {
            CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "Wrong req");
            userSession.close(closeReason);
        }
    }

    private void addToConversation(int conversationId, Session session){
        if (!conversations.containsKey(conversationId)) {
            List<Session> sessions = new ArrayList<>();
            sessions.add(session);
            conversations.put(conversationId, sessions);
        }else {
            conversations.get(conversationId).add(session);
        }
        session.getUserProperties().put("conversation", conversationId);

        mongoMessages.sendLastMessages(conversationId, session);
    }

    @OnClose
    public void onClose(Session userSession) {
        if (userSession.getUserProperties().get("conversation") != null) {
            int conversation = (int) userSession.getUserProperties().get("conversation");

            conversations.get(conversation).remove(userSession);
            if (conversations.get(conversation).size() == 0)
                conversations.remove(conversation);
        }
        System.out.println("Closed session: " + userSession.getId());
    }

    @OnMessage
    public void onMessage(String message, Session userSession) {
        checkFirst(userSession);
        int conversation = (int) userSession.getUserProperties().get("conversation");

        for (Session session : conversations.get(conversation)){
            session.getAsyncRemote().sendText(message);
        }

        mongoMessages.updateConversation(conversation, message);
        mongoMessages.getConversationCollection(conversation).insertOne(Document.parse(message));
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        thr.printStackTrace();
    }

    private void checkFirst(Session userSession) {
        if ((Boolean) userSession.getUserProperties().get("first")) {
            int pdaId = (int) userSession.getUserProperties().get("pda");
            int id = (int) userSession.getUserProperties().get("to");

            int conversationId = ServletContext.mongoMessages.newConversation(pdaId, id);
            addToConversation(conversationId, userSession);
        }

    }

}
