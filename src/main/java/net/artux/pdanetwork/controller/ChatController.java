package net.artux.pdanetwork.controller;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.chat.LimitedArrayList;
import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.models.communication.MessageEntity;
import net.artux.pdanetwork.models.communication.MessageMapper;
import net.artux.pdanetwork.service.member.UserService;
import org.slf4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.sockjs.client.WebSocketClientSockJsSession;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final Logger logger;
    private final UserService userService;
    private final MessageMapper messageMapper;
    private LimitedArrayList<MessageEntity> messageEntities = new LimitedArrayList<>(150);

    /*@MessageMapping("/echo")
    @SendToUser("/topic/echo")
    String echo(String message) {
        logger.info("Echo from " + userService.getMember().getLogin());
        return message;
    }

    @SubscribeMapping("/messages")
    //@SendToUser("/messages")
    List<MessageDTO> messages() {
        return messageMapper.list(messageEntities);
    }

    @MessageMapping("/send")
    @SendTo("/app/topic/catch")
    MessageDTO message(String message) {
        logger.info("Message from " + userService.getMember().getLogin());
        MessageEntity messageEntity = new MessageEntity(userService.getMember(), message);
        messageEntities.add(messageEntity);
        return messageMapper.dto(messageEntity);
    }*/
}
