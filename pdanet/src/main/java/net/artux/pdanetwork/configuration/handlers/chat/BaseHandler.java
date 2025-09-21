package net.artux.pdanetwork.configuration.handlers.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.communication.ChatUpdate;
import net.artux.pdanetwork.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SuperBuilder
@RequiredArgsConstructor
public abstract class BaseHandler implements WebSocketHandler {
    protected final static ChatUpdate EMPTY_UPDATE = ChatUpdate.empty();
    private static final String USER = "user";

    private final Logger logger = LoggerFactory.getLogger(BaseHandler.class);

    private final UserService userService;
    private final ObjectMapper objectMapper;

    private final Map<UUID, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession userSession) {
        UserEntity user = getMember(userSession);
        sessions.put(user.getId(), userSession);
        userSession.getAttributes().put(USER, getMember(userSession));
        handleConnect(userSession);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        reject(session, exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        reject(session, "Code: " + closeStatus.getCode() + ", reason: " + closeStatus.getReason());
    }

    @Override
    public abstract void handleMessage(WebSocketSession session, WebSocketMessage<?> message);
    public abstract void handleConnect(WebSocketSession userSession);
    public abstract void handleClose(WebSocketSession webSocketSession, String message);

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    protected void sendSystemMessage(WebSocketSession userSession, String msg) {
        sendUpdate(userSession, ChatUpdate.event(msg));
    }

    protected void reject(WebSocketSession userSession, String message) {
        UserEntity user = getMember(userSession);
        if(userSession.isOpen()) {
            try {
                if (message != null) sendSystemMessage(userSession, message);
                userSession.close();
            } catch (IOException ignored) {}
        }
        sessions.remove(user.getId());
        logger.info("User {} disconnected from {}.", user.getLogin(), this.getClass().getSimpleName());
        if (message != null && !message.isBlank())
            logger.info("{}: Disconnect reason for {}: {}", this.getClass().getSimpleName(), user.getLogin(), message);
        handleClose(userSession, message);
    }


    protected UserEntity getMember(WebSocketSession userSession) {
        if (userSession == null) return null;
        if (userSession.getAttributes().containsKey(USER)) {
            return (UserEntity) userSession.getAttributes().get(USER);
        }

        Principal principal = userSession.getPrincipal();
        if (principal == null) {
            reject(userSession, "Авторизация не пройдена");
            return null;
        }

        UserEntity userEntity = userService.getUserByLogin(principal.getName());
        userSession.getAttributes().put(USER, userEntity);
        return userEntity;
    }

    protected void sendUpdate(WebSocketSession userSession, ChatUpdate update) {
        sendObject(userSession, update);
    }

    protected void sendObject(WebSocketSession userSession, Object object) {
        if (userSession != null && userSession.isOpen()) {
            try {
                userSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(object)));
            } catch (Exception ignored) {}
        } else reject(userSession, "inactivity");
    }

    protected String getTextMessage(WebSocketMessage<?> message) {
        return message.getPayload().toString();
    }
}
