package net.artux.pdanetwork.configuration.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.artux.pdanetwork.entity.communication.ConversationEntity;
import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.service.communication.ConversationService;
import net.artux.pdanetwork.service.communication.MessagingService;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.utills.ServletHelper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;


@Service
public class MessagesHandler extends SocketHandler {

    private final String CONVERSATION_ATTR = "conv";
    private final String FIRST_ATTR = "first";
    private final String ADDRESS_ATTR = "to";

    private final HashMap<ConversationEntity, Set<WebSocketSession>> conversations = new HashMap<>();

    private final ConversationService conversationService;
    private final MessagingService messagingService;
    private final DialogsHandler dialogsHandler;

    public MessagesHandler(UserService userService, ObjectMapper objectMapper, ConversationService conversationService, MessagingService messagingService, DialogsHandler dialogsHandler) {
        super(userService, objectMapper);
        this.conversationService = conversationService;
        this.messagingService = messagingService;
        this.dialogsHandler = dialogsHandler;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession userSession) {
        super.afterConnectionEstablished(userSession);
        Map<String, String> params = ServletHelper.splitQuery(userSession.getUri().getRawQuery());

        UserEntity user = getMember(userSession);

        if (params.containsKey(ADDRESS_ATTR)) {
            // only private messaging
            int pda2 = Integer.parseInt(params.get(ADDRESS_ATTR));
            ConversationEntity conversation = conversationService.getPrivateConversation(user.getPdaId(), pda2);
            if (conversation != null) {
                addToConversation(conversation, userSession);
            } else {
                //conversation does not exist
                userSession.getAttributes().put(FIRST_ATTR, true);
                userSession.getAttributes().put(ADDRESS_ATTR, pda2);
            }
        } else if (params.containsKey(CONVERSATION_ATTR)) {
            // group or private messaging
            long conversationId = Long.parseLong(params.get(CONVERSATION_ATTR));
            ConversationEntity conversation = conversationService.getPrivateConversation(conversationId, user.getPdaId());
            if (conversation != null) {
                addToConversation(conversation, userSession);
            } else {
                reject(userSession, "Неправильный запрос");
            }
        } else {
            reject(userSession, "Неправильный запрос");
        }
    }

    @Override
    public void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage) {
        createConversationIfNotExists(userSession);
        ConversationEntity conversation = (ConversationEntity) userSession.getAttributes().get(CONVERSATION_ATTR);
        MessageDTO userMessage = messagingService.saveToConversation(webSocketMessage.getPayload().toString(), conversation);//save message

        for (WebSocketSession session : conversations.get(conversation)) {
            sendObject(session, userMessage);
        }
        dialogsHandler.sendUpdates(conversation, userMessage);// send notif
    }

    private void addToConversation(ConversationEntity conversationEntity, WebSocketSession session) {
        if (!conversations.containsKey(conversationEntity)) {
            Set<WebSocketSession> sessions = new HashSet<>();
            sessions.add(session);
            accept(session);
            conversations.put(conversationEntity, sessions);
        } else {
            conversations.get(conversationEntity)
                    .add(session);
        }
        session.getAttributes().put(CONVERSATION_ATTR, conversationEntity);

        List<MessageDTO> messages = messagingService.getLastMessages(conversationEntity, PageRequest.of(0, 20))
                .toList();
        sendObject(session, messages);
    }


    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        if (webSocketSession.getAttributes().get(CONVERSATION_ATTR) != null) {
            ConversationEntity conversation = (ConversationEntity) webSocketSession.getAttributes().get(CONVERSATION_ATTR);

            conversations.get(conversation).remove(webSocketSession);
            if (conversations.get(conversation).size() == 0)
                conversations.remove(conversation);
        }
        super.afterConnectionClosed(webSocketSession, closeStatus);
    }

    private void createConversationIfNotExists(WebSocketSession userSession) {
        ConversationEntity conversation;
        if (userSession.getAttributes().get(FIRST_ATTR) != null) {
            //create new conversation
            int id = (int) userSession.getAttributes().get(ADDRESS_ATTR);

            conversation = conversationService.createPrivateConversation(getMember(userSession).getPdaId(), id);
            addToConversation(conversation, userSession);
        }
    }

}
