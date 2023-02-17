package net.artux.pdanetwork.controller.rest.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.configuration.handlers.ChatHandler;
import net.artux.pdanetwork.configuration.handlers.CommonHandler;
import net.artux.pdanetwork.configuration.handlers.GroupsHandler;
import net.artux.pdanetwork.configuration.handlers.RPHandler;
import net.artux.pdanetwork.configuration.handlers.SocketHandler;
import net.artux.pdanetwork.models.communication.ChatEvent;
import net.artux.pdanetwork.models.communication.ChatStatistic;
import net.artux.pdanetwork.models.communication.ChatUpdate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Tag(name = "Чаты")
@RequestMapping("/utility/chats")
@RequiredArgsConstructor
public class ChatController {

    private final GroupsHandler groupsHandler;
    private final ChatHandler chatHandler;
    private final RPHandler rpHandler;

    @PostMapping("/event")
    public ChatUpdate addEvent(ChatEvent event, @RequestParam("type") ChatType type) {
        SocketHandler socketHandler;
        switch (type) {
            case RP -> socketHandler = rpHandler;
            case GROUP -> socketHandler = groupsHandler;
            default -> socketHandler = chatHandler;
        }
        ChatUpdate update = ChatUpdate.event(event.getContent());
        socketHandler.sendAllUpdate(update);
        return update;
    }

    @DeleteMapping("/deleteMessage")
    public ChatUpdate deleteMessage(@RequestParam("messageId") UUID messageId, @RequestParam("type") ChatType type) {
        CommonHandler socketHandler;
        switch (type) {
            case RP -> socketHandler = rpHandler;
            case GROUP -> socketHandler = groupsHandler;
            default -> socketHandler = chatHandler;
        }
        return socketHandler.deleteMessage(messageId);
    }

    @GetMapping("/statistic")
    public ChatStatistic writeMessage(@RequestParam("type") ChatType type) {
        CommonHandler socketHandler;
        switch (type) {
            case RP -> socketHandler = rpHandler;
            case GROUP -> socketHandler = groupsHandler;
            default -> socketHandler = chatHandler;
        }
        return socketHandler.getStatistic();
    }

    public enum ChatType {
        RP,
        GROUP,
        GENERAL
    }

}
