package net.artux.pdanetwork.configuration.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.artux.pdanetwork.entity.communication.ConversationEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.communication.ConversationDTO;
import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.models.communication.MessageMapper;
import net.artux.pdanetwork.service.communication.ConversationService;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class DialogsHandler extends SocketHandler {

    private final HashMap<UUID, WebSocketSession> sessions = new HashMap<>();
    private final ConversationService conversationService;

    public DialogsHandler(UserService userService, ObjectMapper objectMapper,
                          ConversationService conversationService, MessageMapper messageMapper) {
        super(userService, objectMapper, messageMapper);
        this.conversationService = conversationService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession userSession) {
        super.afterConnectionEstablished(userSession);
        UserEntity userEntity = getMember(userSession);
        sessions.put(userEntity.getId(), userSession);
        sendObject(userSession, getDialogs(userEntity));
    }

    private List<ConversationDTO> getDialogs(UserEntity userEntity) {
        Slice<ConversationDTO> response = conversationService.getConversations(PageRequest.of(0, 20));
        return response.toList();
    }

    public void sendUpdates(ConversationEntity conversationEntity, MessageDTO messageDTO) {
        for (UserEntity userEntity : conversationEntity.getMembers()) {
            UUID uuid = userEntity.getId();
            if (sessions.containsKey(uuid)) {
                sendObject(sessions.get(uuid), messageDTO);
            }
        }
    }


    @Override
    public void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage) {
        //todo send another dialogs
    }

    public void sendError(WebSocketSession session, String message) {
        sendObject(session, new Status(false, message));
    }

}
