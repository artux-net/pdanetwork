package net.artux.pdanetwork.configuration.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.artux.pdanetwork.PDANetworkApplication;
import net.artux.pdanetwork.models.communication.LimitedLinkedList;
import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.models.communication.MessageMapper;
import net.artux.pdanetwork.models.user.dto.UserDto;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.util.ValuesService;
import org.apache.commons.math3.random.ValueServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class ChatHandler extends SocketHandler {

    private static final LimitedLinkedList<MessageDTO> lastMessages = new LimitedLinkedList<>(150);
    private static final HashMap<Long, String> banMap = new HashMap<>();

    private static final Logger logger =
            LoggerFactory.getLogger(PDANetworkApplication.class);
    private static final Timer timer = new Timer();

    private static boolean updateMessages = false;
    private final ValuesService valuesService;

    public ChatHandler(ValuesService valuesService, UserService userService, ObjectMapper objectMapper, MessageMapper messageMapper) {
        super(userService, objectMapper, messageMapper);
        this.valuesService = valuesService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession userSession) {
        super.afterConnectionEstablished(userSession);

        accept(userSession);
        sendObject(userSession, lastMessages);

        if (getMember(userSession).isChatBan())
            reject(userSession, "Вы были заблокированы за нарушение правил.");
        else if (banMap.containsKey(getMember(userSession).getPdaId()))
            reject(userSession, "Вы были временно заблокированы за нарушение правил.");
        else
            sendSystemMessage(userSession, "Вы подключены к чату. <a href=\"" + valuesService.getAddress() + "/rules\">Соблюдайте правила.</a>");
    }

    @Override
    public void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage) {
        MessageDTO message = messageMapper.dto(getMessage(userSession, webSocketMessage));
        UserDto author = message.getAuthor();
        if (banMap.containsKey(author.getPdaId())) {
            sendSystemMessage(userSession, "Вы заблокированы на час. Причина: " + banMap.get(author.getPdaId()));
            return;
        } else if (BadWordsFilter.contains(message.getContent())) {
            sendSystemMessage(userSession, "Мат в общих чатах запрещен. На вас наложен временный бан.");
            banUser(author.getPdaId(), "Автоматический бан за использование плохих слов.");
            logger.info("Bad word detected, " + author.getLogin() + " pdaId: " + author.getPdaId() + ", message: " + message.getContent());
            return;
        }
        lastMessages.add(message);
        for (WebSocketSession session : getSessions()) {
            if (updateMessages) {
                sendObject(session, lastMessages);
                updateMessages = false;
            } else sendObject(session, message);
        }
    }


    public void banUser(Long pdaId, String reason) {
        banMap.put(pdaId, reason);
        logger.info("pdaId: " + pdaId + " banned for hour in general chat: " + reason);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                banMap.remove(pdaId);
            }
        }, 60 * 60 * 1000);
    }

    public void removeMessage(Long time) {
        lastMessages.removeIf(userMessage -> userMessage.getTimestamp().toEpochMilli() == time);
        updateMessages = true;
    }

}
