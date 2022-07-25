package net.artux.pdanetwork.configuration.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.conversation.ConversationEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.communication.ConversationDTO;
import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.service.communication.ConversationService;
import net.artux.pdanetwork.service.member.UserService;
import net.artux.pdanetwork.service.profile.ProfileService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;

@Service
public class DialogsHandler extends SocketHandler {

    private final HashMap<Long, WebSocketSession> sessions = new HashMap<>();
    private final ConversationService conversationService;

    public DialogsHandler(UserService userService, ObjectMapper objectMapper, ConversationService conversationService) {
        super(userService, objectMapper);
        this.conversationService = conversationService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession userSession) {
        super.afterConnectionEstablished(userSession);
        UserEntity userEntity = getMember(userSession);
        sessions.put(userEntity.getPdaId(), userSession);
        sendObject(userSession, getDialogs(userEntity));
    }

    private List<ConversationDTO> getDialogs(UserEntity userEntity) {
        Slice<ConversationDTO> response = conversationService.getConversations(PageRequest.of(0, 20));
        return response.toList();
    }

    public void sendUpdates(ConversationEntity conversationEntity, MessageDTO messageDTO) {
        for (UserEntity userEntity : conversationEntity.getMembers()) {
            long pdaId = userEntity.getId();
            if (sessions.containsKey(pdaId)) {
                sendObject(sessions.get(pdaId), messageDTO);
            }
        }
    }


    @Override
    public void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage) {

    }

    public void sendError(WebSocketSession session, String message) {
        sendObject(session, new Status(false, message));
    }

}
