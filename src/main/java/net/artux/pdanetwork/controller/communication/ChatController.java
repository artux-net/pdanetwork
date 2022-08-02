package net.artux.pdanetwork.controller.communication;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.communication.LimitedLinkedList;
import net.artux.pdanetwork.entity.communication.MessageEntity;
import net.artux.pdanetwork.models.communication.MessageMapper;
import net.artux.pdanetwork.service.user.UserService;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final Logger logger;
    private final UserService userService;
    private final MessageMapper messageMapper;
    private LimitedLinkedList<MessageEntity> messageEntities = new LimitedLinkedList<>(150);

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
