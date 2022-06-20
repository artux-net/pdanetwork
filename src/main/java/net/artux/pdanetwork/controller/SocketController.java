package net.artux.pdanetwork.controller;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.communication.model.LimitedArrayList;
import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.models.communication.MessageEntity;
import net.artux.pdanetwork.models.communication.MessageMapper;
import net.artux.pdanetwork.service.member.UserService;
import org.slf4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SocketController {

    private final Logger logger;
    private final UserService userService;
    private final MessageMapper messageMapper;
    private LimitedArrayList<MessageEntity> messageEntities = new LimitedArrayList<>(150);

    @MessageMapping("/echo")
    @SendToUser("/topic/echo")
    String echo(String message) {
        logger.info("Echo from " + userService.getMember().getLogin());
        return message;
    }

    @MessageMapping("/messages")
    @SendToUser("/topic/messages")
    List<MessageDTO> messages() {
        return messageMapper.list(messageEntities);
    }

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    MessageDTO message(String message) {
        logger.info("Message from " + userService.getMember().getLogin());
        MessageEntity messageEntity = new MessageEntity(userService.getMember(), message);
        messageEntities.add(messageEntity);
        return messageMapper.dto(messageEntity);
    }

}
