package net.artux.pdanetwork.configuration.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.communication.ChatUpdate;
import net.artux.pdanetwork.models.communication.LimitedLinkedList;
import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.models.communication.MessageMapper;
import net.artux.pdanetwork.models.user.UserMapper;
import net.artux.pdanetwork.models.user.gang.Gang;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.user.ban.BanService;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.UUID;

@Service
public class GroupsHandler extends CommonHandler {

    private final HashMap<Gang, LimitedLinkedList<MessageDTO>> lastMessages = initLastMessages();
    private static final int limit = 150;

    public GroupsHandler(UserService userService, ObjectMapper objectMapper, MessageMapper messageMapper, ValuesService valuesService, BanService banService, UserMapper userMapper) {
        super(userService, objectMapper, messageMapper, valuesService, banService, userMapper);
    }

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

    @Override
    public void afterConnectionEstablished(WebSocketSession userSession) {
        UserEntity userEntity = getMember(userSession);
        Gang gang = userEntity.getGang();
        if (gang != Gang.LONERS) {
            sendUpdate(userSession, ChatUpdate.of(lastMessages.get(gang)));
        } else {
            reject(userSession, "Ваш PDA не подключен ни к одной из групп");
        }
    }

    @Override
    public void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage) {
        Gang gang = getMember(userSession).getGang();
        String text = getTextMessage(webSocketMessage);
        ChatUpdate update = getUpdate(userSession, text);
        lastMessages.get(gang).addAll(update.getUpdates());

        for (WebSocketSession session : getSessions())
            if (getMember(session).getGang() == gang)
                sendUpdate(session, update);
    }

    @Override
    protected MessageDTO getDeletedMessage(UUID messageId) {
        MessageDTO[] messages = new MessageDTO[1];
        lastMessages.values().forEach(list ->
                list.removeIf(messageDTO -> {
                            if (messageDTO.getId().equals(messageId)) {
                                messages[0] = messageDTO;
                                return true;
                            }
                            return false;
                        }
                ));
        return messages[0];
    }

}
