package net.artux.pdanetwork.configuration.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.entity.user.gang.GangRelationEntity;
import net.artux.pdanetwork.models.communication.LimitedLinkedList;
import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.models.communication.MessageMapper;
import net.artux.pdanetwork.models.user.gang.Gang;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

@Service
public class GroupsHandler extends SocketHandler {

    private HashMap<Gang, LimitedLinkedList<MessageDTO>> lastMessages = initLastMessages();
    private static final int limit = 150;

    private HashMap<Gang, LimitedLinkedList<MessageDTO>> initLastMessages() {
        HashMap<Gang, LimitedLinkedList<MessageDTO>> lastMessages = new HashMap<>();

        lastMessages.put(Gang.LONERS, new LimitedLinkedList<>(limit));
        lastMessages.put(Gang.BANDITS, new LimitedLinkedList<>(limit));
        lastMessages.put(Gang.DUTY, new LimitedLinkedList<>(limit));
        lastMessages.put(Gang.CLEAR_SKY, new LimitedLinkedList<>(limit));
        lastMessages.put(Gang.LIBERTY, new LimitedLinkedList<>(limit));
        lastMessages.put(Gang.MILITARY, new LimitedLinkedList<>(limit));

        return lastMessages;
    }

    public GroupsHandler(UserService userService, ObjectMapper mapper, MessageMapper messageMapper) {
        super(userService, mapper, messageMapper);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession userSession) {
        super.afterConnectionEstablished(userSession);

        UserEntity userEntity = getMember(userSession);
        GangRelationEntity gang = userEntity.getGangRelation();
        if (gang != null) {
            sendObject(userSession, lastMessages.get(gang));
        } else {
            sendSystemMessage(userSession, "Ваш PDA не подключен ни к одной из групп");
        }
    }

    @Override
    public void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage) {
        Gang gang = getMember(userSession).getGang();
        MessageDTO messageEntity = messageMapper.dto(getMessage(userSession, webSocketMessage));
        lastMessages.get(gang).add(messageEntity);

        for (WebSocketSession session : getSessions())
            if (getMember(session).getGang() == gang)
                sendObject(session, messageEntity);
    }

}
