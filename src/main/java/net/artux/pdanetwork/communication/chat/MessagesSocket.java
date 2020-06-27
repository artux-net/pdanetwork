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
        Map<String, String> params = RequestReader.splitQuery(userSession.getQueryString());

        String token = (String) config.getUserProperties().get("t");
        int pdaId = ServletContext.mongoUsers.getPdaIdByToken(token);

        userSession.getUserProperties().put("t", token);
        userSession.getUserProperties().put("pda", pdaId);
        int conversationId;
        if (params.containsKey("to")){
            int toPdaId = Integer.parseInt(params.get("to"));
            //TODO: id doesn't exists
            int id = mongoMessages.getDialogID(pdaId, toPdaId);
            if (id == 0){
                conversationId = mongoMessages.newConversation(pdaId, toPdaId);
                addToConversation(conversationId, userSession);
            } else {
                conversationId = id;
                addToConversation(conversationId, userSession);

                mongoMessages.sendLastMessages(conversationId, userSession);
            }
        }else if (params.containsKey("c")){
            conversationId = Integer.parseInt(params.get("c"));
            if (mongoMessages.hasConversation(conversationId)
                    && mongoMessages.conversationHas(conversationId,pdaId)){
                addToConversation(conversationId, userSession);
                mongoMessages.sendLastMessages(conversationId, userSession);
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
    }

    @OnClose
    public void onClose(Session userSession) {
        int conversation = (int) userSession.getUserProperties().get("conversation");

        conversations.get(conversation).remove(userSession);
        if (conversations.get(conversation).size()==0)
            conversations.remove(conversation);
    }

    @OnMessage
    public void onMessage(String message, Session userSession) {
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

}
