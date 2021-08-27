package net.artux.pdanetwork.communication.chat;


import com.google.gson.Gson;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.communication.model.Conversation;
import net.artux.pdanetwork.communication.model.UserMessage;
import net.artux.pdanetwork.communication.utilities.MongoMessages;
import net.artux.pdanetwork.utills.ServletContext;
import net.artux.pdanetwork.utills.ServletHelper;
import net.artux.pdanetwork.service.util.ValuesService;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

import static net.artux.pdanetwork.utills.ServletContext.mongoUsers;

@ServerEndpoint(value = "/dialog")
public class MessagesSocket {

    private final HashMap<Integer, List<Session>> conversations = new HashMap<>();

    private final MongoMessages mongoMessages = new MongoMessages(new ValuesService());
    Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session userSession, EndpointConfig config) throws IOException {
        Map<String, String> params = ServletHelper.splitQuery(userSession.getQueryString());

        String token = (String) config.getUserProperties().get("t");
        Member member = mongoUsers.getByToken(token);
        if (member != null) {
            int pdaId = member.getPdaId();

            ServletContext.log("Opened session(" + userSession.getId() + ") for pda: " + pdaId + ", query: "
                    + userSession.getQueryString());

            userSession.getUserProperties().put("m", member);
            userSession.getUserProperties().put("pda", pdaId);
            if (params.containsKey("to")) {
                // private message
                int id = Integer.parseInt(params.get("to"));
                int conversationId = mongoMessages.getDialogID(pdaId, id);
                if (conversationId != 0) {
                    addToConversation(conversationId, userSession);
                } else {
                    //conv does not exist
                    userSession.getUserProperties().put("first", true);
                    userSession.getUserProperties().put("pda", pdaId);
                    userSession.getUserProperties().put("to", id);
                }
            } else if (params.containsKey("c")) {
                // group message
                int conversationId = Integer.parseInt(params.get("c"));
                if (mongoMessages.hasConversation(conversationId)
                        && mongoMessages.conversationHas(conversationId, pdaId)) {
                    addToConversation(conversationId, userSession);
                } else {
                    CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "Wrong req");
                    ServletContext.log("Can not connect pdaId " + pdaId + " to conversation " + conversationId);
                    Conversation conversation = mongoMessages.getConversation(conversationId);
                    if (conversation != null) {
                        ServletContext.log(Arrays.toString(conversation.allMembers().toArray()) + " does not have this id");
                    } else
                        ServletContext.log("This conversation does not exist");

                    userSession.close(closeReason);
                }
            } else {
                CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "Wrong req");
                ServletContext.log("Session does not have query");
                userSession.close(closeReason);
            }
        } else {
            ServletContext.log("Messages: failed for auth token " + token);
            userSession.close(new CloseReason(CloseReason.CloseCodes.TRY_AGAIN_LATER, "Auth failed"));
        }
    }

    private void addToConversation(int conversationId, Session session) throws IOException {
        if (!conversations.containsKey(conversationId)) {
            List<Session> sessions = new ArrayList<>();
            sessions.add(session);
            conversations.put(conversationId, sessions);
        } else {
            conversations.get(conversationId)
                    .add(session);
        }
        session.getUserProperties().put("conversation", conversationId);

        List<UserMessage> messages = mongoMessages.getLastMessages(conversationId, 30, 0);
        session.getBasicRemote().sendText(gson.toJson(messages));
    }

    @OnClose
    public void onClose(Session userSession) {
        if (userSession.getUserProperties().get("conversation") != null) {
            int conversation = (int) userSession.getUserProperties().get("conversation");

            conversations.get(conversation).remove(userSession);
            if (conversations.get(conversation).size() == 0)
                conversations.remove(conversation);
        }
        ServletContext.log("Closed session: " + userSession.getId());
    }


    @OnMessage
    public void onMessage(String message, Session userSession) throws IOException {
        int conversation = (int) userSession.getUserProperties().get("conversation");
        UserMessage userMessage = new UserMessage(conversation, (Member) userSession.getUserProperties().get("m"), message);

        for (Session session : conversations.get(conversation)) {
            session.getAsyncRemote().sendText(gson.toJson(userMessage));
        }

        mongoMessages.addMessageToConversation(conversation, userMessage);
        updateDialog(userSession, userMessage);
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        ServletContext.error("onError - MessagesSocket for session-cid: " + session.getId(), thr);
    }

    private void updateDialog(Session userSession, UserMessage message) throws IOException {
        if (userSession.getUserProperties().get("first") != null &&
                (Boolean) userSession.getUserProperties().get("first")) {
            //add new conv
            int pdaId = (int) userSession.getUserProperties().get("pda");
            int id = (int) userSession.getUserProperties().get("to");

            Conversation conversation = ServletContext.mongoMessages.newConversation(pdaId, id);
            addToConversation(conversation.getCid(), userSession);
            for (int ids : conversation.allMembers()) {
                mongoUsers.updateDialog(pdaId, conversation.cid);
                //SocketConfigurator.dialogsSocket.sendUpdate(ids, gson.toJson(message));
            }
        } else {
            //up conv
            Conversation conversation = mongoMessages
                    .getConversation((int) userSession.getUserProperties().get("conversation"));
            for (int ids : conversation.allMembers()) {
                mongoUsers.updateDialog(ids, conversation.cid);
                //SocketConfigurator.dialogsSocket.sendUpdate(ids, gson.toJson(message));
            }
        }
    }

}
