package net.artux.pdanetwork.configuration.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.artux.pdanetwork.PDANetworkApplication;
import net.artux.pdanetwork.models.communication.LimitedLinkedList;
import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.models.communication.MessageMapper;
import net.artux.pdanetwork.models.user.dto.UserDto;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.user.ban.BanService;
import net.artux.pdanetwork.service.util.ValuesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
public class ChatHandler extends SocketHandler {

    private static final LimitedLinkedList<MessageDTO> lastMessages = new LimitedLinkedList<>(150);

    private static final Logger logger =
            LoggerFactory.getLogger(PDANetworkApplication.class);

    private static boolean updateMessages = false;
    private final ValuesService valuesService;
    private final BanService banService;

    public ChatHandler(ValuesService valuesService, UserService userService, ObjectMapper objectMapper, MessageMapper messageMapper, BanService banService) {
        super(userService, objectMapper, messageMapper);
        this.valuesService = valuesService;
        this.banService = banService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession userSession) {
        super.afterConnectionEstablished(userSession);

        accept(userSession);

        if (getMember(userSession).isChatBan())
            reject(userSession, "Вы были заблокированы за нарушение правил.");
        else if (banService.isBanned(getMember(userSession).getId()))
            reject(userSession, "Вы были временно заблокированы за нарушение правил.");
        else {
            sendSystemMessage(userSession, "Вы подключены к чату. <a href=\"" + valuesService.getAddress() + "/rules\">Соблюдайте правила.</a>");
            sendObject(userSession, lastMessages);
        }
    }

    @Override
    public void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage) {
        MessageDTO message = messageMapper.dto(getMessage(userSession, webSocketMessage));
        UserDto author = message.getAuthor();
        if (banService.isBanned(author.getId())) {
            sendSystemMessage(userSession, "Заблокирована отправка сообщений. Причина: " + banService.getCurrentBan(author.getId()).getReason());
            return;
        } else if (BadWordsFilter.contains(message.getContent())) {
            sendSystemMessage(userSession, "Мат в общих чатах запрещен. На вас наложен временный бан.");
            banService.applyBan(author.getId(), "Автоматический бан за использование плохих слов.", message.getContent(), 60 * 15);
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

    public void removeMessage(Long time) {
        lastMessages.removeIf(userMessage -> userMessage.getTimestamp().toEpochMilli() == time);
        updateMessages = true;
    }

}
