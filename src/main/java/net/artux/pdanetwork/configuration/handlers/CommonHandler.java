package net.artux.pdanetwork.configuration.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.communication.ChatEvent;
import net.artux.pdanetwork.models.communication.ChatUpdate;
import net.artux.pdanetwork.models.communication.LimitedLinkedList;
import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.models.communication.MessageMapper;
import net.artux.pdanetwork.models.user.ban.BanDto;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.user.ban.BanService;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


public abstract class CommonHandler extends SocketHandler {

    private final static ChatUpdate EMPTY_UPDATE = ChatUpdate.empty();

    private final LimitedLinkedList<MessageDTO> lastMessages;
    private final ValuesService valuesService;
    private final BanService banService;

    public CommonHandler(UserService userService, ObjectMapper objectMapper, MessageMapper messageMapper, ValuesService valuesService, BanService banService) {
        super(userService, objectMapper, messageMapper);
        this.valuesService = valuesService;
        this.banService = banService;
        lastMessages = new LimitedLinkedList<>(150);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession userSession) {
        super.afterConnectionEstablished(userSession);

        accept(userSession);

        if (getMember(userSession).isChatBan()) {
            reject(userSession, "Вы были заблокированы за нарушение правил.");
            return;
        }
        ChatUpdate initialUpdate = ChatUpdate.of(lastMessages);
        initialUpdate.addEvent(ChatEvent.of("Вы подключены к чату. <a href=\"" + valuesService.getAddress() + "/rules\">Соблюдайте правила.</a>"));
        if (banService.isBanned(getMember(userSession).getId()))
            initialUpdate.addEvent(ChatEvent.of("Отправка сообщений временно заблокирована."));
        sendUpdate(userSession, initialUpdate);
    }

    @Override
    public void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage) {
        String message = getTextMessage(webSocketMessage);
        UserEntity author = getMember(userSession);

        if (!message.isBlank()) {
            ChatUpdate update;
            if (banService.isBanned(author.getId())) {
                BanDto ban = banService.getCurrentBan(author.getId());
                Instant endTime = ban.getTimestamp().plusSeconds(ban.getSeconds());
                update = ChatUpdate.event("Заблокирована отправка сообщений. Причина: "
                        + ban.getReason() + ", осталось времени: " + ChronoUnit.SECONDS.between(Instant.now(), endTime) + " сек.");
                sendUpdate(userSession, update);
                return;
            }

            if (BadWordsFilter.contains(message)) {
                sendSystemMessage(userSession, "Мат в общих чатах запрещен. На вас наложен временный бан.");
                banService.applySystemBan(author.getId(), "Автоматический бан за использование плохих слов.", message, 60 * 15);
                update = ChatUpdate.event(author.getName() + " " + author.getNickname() + " временно заблокирован за нарушение правил.");
            } else
                update = getUpdate(userSession, message);

            applyUpdate(update);
        } else {
            sendUpdate(userSession, EMPTY_UPDATE);
        }
    }

    protected void applyUpdate(ChatUpdate update) {
        for (WebSocketSession session : getSessions()) {
            sendUpdate(session, update);
        }
        lastMessages.addAll(update.asOld().getUpdates());
    }

}
