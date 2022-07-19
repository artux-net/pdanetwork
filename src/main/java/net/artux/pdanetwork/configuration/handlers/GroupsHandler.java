package net.artux.pdanetwork.configuration.handlers;

import net.artux.pdanetwork.models.chat.LimitedArrayList;
import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.models.communication.MessageEntity;
import net.artux.pdanetwork.models.gang.Gang;
import net.artux.pdanetwork.models.achievement.GangRelationEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.service.member.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

@Service
public class GroupsHandler extends SocketHandler {

    private HashMap<Gang, LimitedArrayList<MessageDTO>> lastMessages = initLastMessages();
    private static final int limit = 150;

    private HashMap<Gang, LimitedArrayList<MessageDTO>> initLastMessages() {
        HashMap<Gang, LimitedArrayList<MessageDTO>> lastMessages = new HashMap<>();

        lastMessages.put(Gang.LONERS, new LimitedArrayList<>(limit));
        lastMessages.put(Gang.BANDITS, new LimitedArrayList<>(limit));
        lastMessages.put(Gang.DUTY, new LimitedArrayList<>(limit));
        lastMessages.put(Gang.CLEAR_SKY, new LimitedArrayList<>(limit));
        lastMessages.put(Gang.LIBERTY, new LimitedArrayList<>(limit));
        lastMessages.put(Gang.MILITARY, new LimitedArrayList<>(limit));

        return lastMessages;
    }

    public GroupsHandler(UserService userService) {
        super(userService);
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
        MessageDTO messageEntity = getMessage(userSession, webSocketMessage);
        lastMessages.get(gang).add(messageEntity);

        for (WebSocketSession session : getSessions())
            if (getMember(session).getGang() == gang)
                sendObject(session, messageEntity);
    }

}
