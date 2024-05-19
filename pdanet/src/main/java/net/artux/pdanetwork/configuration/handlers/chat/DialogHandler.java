package net.artux.pdanetwork.configuration.handlers.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.communication.ChatUpdate;
import net.artux.pdanetwork.models.communication.ConversationDTO;
import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.entity.mappers.UserMapper;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;
import net.artux.pdanetwork.models.user.dto.UserDto;
import net.artux.pdanetwork.service.communication.ConversationService;
import net.artux.pdanetwork.service.communication.MessagingService;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.utills.ServletHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class DialogHandler extends BaseHandler {
    private final Logger logger = LoggerFactory.getLogger(BaseHandler.class);
    private final static String CHAT_ID = "chatId";
    private final static String CONVERSATION = "conversation";

    private final Map<UUID, Map<UUID, WebSocketSession>> sessions = new ConcurrentHashMap<>();
    private final UserMapper userMapper;
    private final MessagingService messagingService;
    private final ConversationService conversationService;

    public DialogHandler(UserService userService, ObjectMapper objectMapper, UserMapper userMapper, MessagingService messagingService, ConversationService conversationService) {
        super(userService, objectMapper);
        this.userMapper = userMapper;
        this.messagingService = messagingService;
        this.conversationService = conversationService;
    }


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) {
        ConversationDTO conversation;
        try {
            conversation = getConversation(session);
        } catch (Exception e) {
            reject(session, e.getMessage());
            return;
        }
        session.getAttributes().put(CONVERSATION, conversation);

        UserEntity user = getMember(session);
        String message = getTextMessage(webSocketMessage);

        if (!message.isBlank()) {
            UserDto userDto = userMapper.dto(user);
            MessageDTO messageDTO = new MessageDTO(userDto, message);

            ChatUpdate chatUpdate = new ChatUpdate(List.of(messageDTO), List.of());
            applyUpdate(conversation.getId(), chatUpdate);

            messagingService.saveMessageToConversation(messageDTO, conversation, user);
        } else {
            sendUpdate(session, EMPTY_UPDATE);
        }
    }

    @Override
    public void handleConnect(WebSocketSession userSession) {
        UserEntity user = getMember(userSession);
        ConversationDTO conversation;
        try {
            conversation = getConversation(userSession);
        } catch (Exception e) {
            reject(userSession, e.getMessage());
            return;
        }

        userSession.getAttributes().put(CONVERSATION, conversation);

        logger.info("User {} connect to `{}` conversation", user.getLogin(), conversation.getTitle());

        if (!sessions.containsKey(conversation.getId())) {
            Map<UUID, WebSocketSession> newSessionMap = new HashMap<>();
            newSessionMap.put(user.getId(), userSession);
            sessions.put(conversation.getId(), newSessionMap);
        } else {
            sessions.get(conversation.getId()).put(user.getId(), userSession);
        }

        ChatUpdate chatUpdate = new ChatUpdate(messagingService.getMessagesByConversationId(conversation.getId()), List.of());

        try {
            sendObject(userSession, chatUpdate.asOld());
        } catch (Exception ignored) {}
    }

    @Override
    public void handleClose(WebSocketSession session, String message) {
        UserEntity user = getMember(session);
        try {
            ConversationDTO conversation = getConversation(session);
            sessions.get(conversation.getId()).remove(user.getId());
        } catch (Exception ignored) {}
    }

    private void applyUpdate(UUID conversationId, ChatUpdate update) {
        Map<UUID, WebSocketSession> sessionList = sessions.get(conversationId);
        for (WebSocketSession session : sessionList.values()) {
            sendUpdate(session, update);
        }
    }

    private ConversationDTO getConversation(WebSocketSession session) throws Exception {
        if(session.getAttributes().containsKey(CONVERSATION)) {
            return (ConversationDTO) session.getAttributes().get(CONVERSATION);
        }

        Map<String, String> query = getQueryMap(session);
        if (!query.containsKey(CHAT_ID)) {
            throw new Exception("`chatId` in query is null");
        }

        UUID conversationId;
        try {
            conversationId = UUID.fromString(query.get(CHAT_ID));
        } catch (IllegalArgumentException e) {
            throw new Exception("conversation uuid is not valid");
        }
        UserEntity user = getMember(session);
        ConversationDTO conversation = conversationService.getConversationEntity(conversationId);

        if (conversation == null) {
            throw new Exception("not found conversation");
        } else if (!conversation.getMembers().stream().map(SimpleUserDto::getId).toList().contains(user.getId())) {
            throw new Exception("user is not in the conversation");
        }

        return conversation;
    }

    private Map<String, String> getQueryMap(WebSocketSession userSession) throws Exception {
        String query = userSession.getUri().getQuery();
        if (query == null) {
            throw new Exception("query is null");
        }
        return ServletHelper.splitQuery(query);
    }
}
