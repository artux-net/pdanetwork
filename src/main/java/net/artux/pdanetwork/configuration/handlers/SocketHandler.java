package net.artux.pdanetwork.configuration.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.communication.MessageEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.communication.MessageMapper;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;


@RequiredArgsConstructor
public abstract class SocketHandler implements WebSocketHandler {

    private final UserService userService;
    private static final List<WebSocketSession> sessionList = new ArrayList<>();
    private final ObjectMapper objectMapper;
    protected final MessageMapper messageMapper;

    private static final String USER = "user";

    @Override
    public void afterConnectionEstablished(WebSocketSession userSession) {
        userSession.getAttributes().put(USER, getMember(userSession));
    }

    protected void accept(WebSocketSession userSession) {
        sessionList.add(userSession);
    }

    protected void reject(WebSocketSession userSession, String message) {
        if (userSession.isOpen()) {
            try {
                if (!message.isBlank())
                    sendSystemMessage(userSession, message);
                userSession.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sessionList.remove(userSession);
    }

    protected static List<WebSocketSession> getSessions() {
        return sessionList;
    }

    protected void sendSystemMessageByPdaId(Integer pdaId, String msg) {
        Optional<WebSocketSession> webSocketSessionOptional = sessionList.stream().filter(new Predicate<WebSocketSession>() {
            @Override
            public boolean test(WebSocketSession webSocketSession) {
                return getMember(webSocketSession).getPdaId() == pdaId;
            }
        }).findFirst();
        if (webSocketSessionOptional.isPresent())
            sendSystemMessage(webSocketSessionOptional.get(), msg);

    }

    protected UserEntity getMember(WebSocketSession userSession) {
        if (userSession.getAttributes().containsKey(USER))
            return (UserEntity) userSession.getAttributes().get(USER);
        Principal principal = userSession.getPrincipal();
        if (principal != null) {
            UserEntity userEntity = userService.getUserByLogin(userSession.getPrincipal().getName());
            userSession.getAttributes().put(USER, userEntity);
            return userEntity;
        } else {
            reject(userSession, "Авторизация не пройдена");
            throw new RuntimeException("Авторизация не пройдена");
        }
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
        sendObject(userSession, MessageEntity.getSystemMessage("ru", msg));
    }

    protected <T> T get(Class<T> clazz, WebSocketMessage<?> message) {
        try {
            return objectMapper.readValue(message.getPayload().toString(), clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected MessageEntity getMessage(WebSocketSession userSession, WebSocketMessage<?> message) {
        return new MessageEntity(getMember(userSession), message.getPayload().toString());
    }

    @Override
    public abstract void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage);

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        reject(webSocketSession, null);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }
}
