package net.artux.pdanetwork.controller.rest.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.configuration.handlers.*;
import net.artux.pdanetwork.models.communication.ChatEvent;
import net.artux.pdanetwork.models.communication.ChatStatistic;
import net.artux.pdanetwork.models.communication.ChatUpdate;
import net.artux.pdanetwork.utills.security.ModeratorAccess;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Чаты", description = "Доступен с роли модератора")
@RequestMapping("/api/v1/admin/chats")
@ModeratorAccess
@RequiredArgsConstructor
public class ChatController {

    private final GroupsHandler groupsHandler;
    private final ChatHandler chatHandler;
    private final RPHandler rpHandler;

    @PostMapping("/event")
    @Operation(description = "Добавление события в чат")
    public ChatUpdate addEvent(@RequestParam("type") ChatType type, @RequestBody ChatEvent event) {
        SocketHandler socketHandler = switch (type) {
            case RP -> rpHandler;
            case GROUP -> groupsHandler;
            default -> chatHandler;
        };

        ChatUpdate update = ChatUpdate.event(event.getContent());
        socketHandler.sendAllUpdate(update);
        return update;
    }

    @DeleteMapping("/deleteMessage")
    @Operation(description = "Удаление сообщения из чата")
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
    @Operation(description = "Получение текущей статистики чата")
    public ChatStatistic writeMessage(@RequestParam("type") ChatType type) {
        CommonHandler socketHandler;
        switch (type) {
            case RP -> socketHandler = rpHandler;
            case GROUP -> socketHandler = groupsHandler;
            default -> socketHandler = chatHandler;
        }
        return socketHandler.getStatistic();
    }

    @GetMapping("/types")
    @Operation(description = "Получение типов чатов")
    public ChatType[] getTypes() {
        return ChatType.values();
    }

    public enum ChatType {
        RP,
        GROUP,
        GENERAL
    }

}
