package net.artux.pdanetwork.configuration.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.communication.MessageEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.communication.ChatUpdate;
import net.artux.pdanetwork.models.communication.MessageMapper;
import net.artux.pdanetwork.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class SocketHandler implements WebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(SocketHandler.class);

    private final UserService userService;
    private final HashMap<UUID, WebSocketSession> sessionMap = new HashMap<>();
    private final ObjectMapper objectMapper;
    protected final MessageMapper messageMapper;

    private static final String USER = "user";

    @Override
    public void afterConnectionEstablished(WebSocketSession userSession) {
        userSession.getAttributes().put(USER, getMember(userSession));
    }

    protected void accept(WebSocketSession userSession) {
        UserEntity user = getMember(userSession);
        if (sessionMap.containsKey(user.getId()))
            reject(sessionMap.get(user.getId()), "Another client connected");

        sessionMap.put(user.getId(), userSession);
        logger.info("{}: User {} connected to chat.", this.getClass().getSimpleName(), getMember(userSession).getLogin());
    }

    protected void reject(WebSocketSession userSession, String message) {
        UserEntity user = getMember(userSession);
        if (userSession.isOpen()) {
            try {
                if (message != null && !message.isBlank())
                    sendSystemMessage(userSession, message);
                userSession.close();
            } catch (IOException ignored) {}
        }
        sessionMap.remove(user.getId());
        logger.info("User {} disconnected.", this.getClass().getSimpleName(), user.getLogin());
        if (message != null && !message.isBlank())
            logger.info("{}: Disconnect reason for {}: ", this.getClass().getSimpleName(), user.getLogin(), message);
    }

    protected Collection<WebSocketSession> getSessions() {
        return sessionMap.values();
    }

    public void sendAllUpdate(ChatUpdate update) {
        logger.debug("{}: Send message to {} sessions. Update contains {} messages, {} events.",
                this.getClass().getSimpleName(), getSessions().size(),
                update.getUpdates().size(), update.getEvents().size());
        for (WebSocketSession session : getSessions()) {
            sendUpdate(session, update);
        }
    }

    protected UserEntity getMember(WebSocketSession userSession) {
        if (userSession == null)
            return null;
        if (userSession.getAttributes().containsKey(USER))
            return (UserEntity) userSession.getAttributes().get(USER);
        Principal principal = userSession.getPrincipal();
        if (principal != null) {
            UserEntity userEntity = userService.getUserByLogin(principal.getName());
            userSession.getAttributes().put(USER, userEntity);
            return userEntity;
        } else {
            reject(userSession, "Авторизация не пройдена");
            throw new RuntimeException("Авторизация не пройдена");
        }
    }

    protected List<UserEntity> getActiveUsers(){
        return userService.getUsersByIds(sessionMap.keySet());
    }

    protected void sendUpdate(WebSocketSession userSession, ChatUpdate update) {
        sendObject(userSession, update);
    }

    protected void sendObject(WebSocketSession userSession, Object object) {
        if (userSession != null && userSession.isOpen())
            try {
                userSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(object)));
            } catch (Exception ignored) {

            }
        else reject(userSession, "inactivity");
    }

    protected void sendSystemMessage(WebSocketSession userSession, String msg) {
        sendUpdate(userSession, ChatUpdate.event(msg));
    }

    protected <T> T get(Class<T> clazz, WebSocketMessage<?> message) {
        try {
            return objectMapper.readValue(message.getPayload().toString(), clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String getTextMessage(WebSocketMessage<?> message) {
        return message.getPayload().toString();
    }

    protected ChatUpdate getUpdate(WebSocketSession userSession, String textMessage) {
        if (textMessage.isBlank())
            return ChatUpdate.empty();
        else {
            logger.debug("{}: Creating chat-update \"{}\" from {}", this.getClass().getSimpleName(), textMessage, getMember(userSession).getLogin());
            return ChatUpdate.of(messageMapper.dto(new MessageEntity(getMember(userSession), textMessage)));
        }
    }


    @Override
    public abstract void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage);

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        reject(webSocketSession, "Code: " + closeStatus.getCode() + ", reason: " + closeStatus.getReason());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) {
        reject(webSocketSession, throwable.getMessage());
    }
}
