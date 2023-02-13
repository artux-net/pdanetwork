package net.artux.pdanetwork.controller.rest.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.configuration.handlers.ChatHandler;
import net.artux.pdanetwork.configuration.handlers.GroupsHandler;
import net.artux.pdanetwork.configuration.handlers.RPHandler;
import net.artux.pdanetwork.configuration.handlers.SocketHandler;
import net.artux.pdanetwork.models.communication.ChatUpdate;
import net.artux.pdanetwork.models.user.ban.BanDto;
import net.artux.pdanetwork.service.user.ban.BanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
    public ChatUpdate addEvent(String event, ChatType type) {
        SocketHandler socketHandler;
        switch (type){
            case RP -> socketHandler = rpHandler;
            case GROUP -> socketHandler = groupsHandler;
            default -> socketHandler = chatHandler;
        }
        ChatUpdate update = ChatUpdate.event(event);
        socketHandler.sendAllUpdate(update);
        return update;
    }

    public enum ChatType{
        RP,
        GROUP,
        GENERAL
    }

}
