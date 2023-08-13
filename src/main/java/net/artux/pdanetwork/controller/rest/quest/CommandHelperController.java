package net.artux.pdanetwork.controller.rest.quest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.command.ClientCommand;
import net.artux.pdanetwork.models.command.ServerCommand;
import net.artux.pdanetwork.models.enums.CommandDto;
import net.artux.pdanetwork.models.enums.EnumDto;
import net.artux.pdanetwork.models.enums.EnumGetter;
import net.artux.pdanetwork.models.enums.EnumMapper;
import net.artux.pdanetwork.models.quest.StoryInfo;
import net.artux.pdanetwork.service.quest.QuestService;
import net.artux.pdanetwork.utills.security.ModeratorAccess;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Tag(name = "Список поддерживаемых команд")
@RequestMapping("/api/v1/commands")
@ModeratorAccess
public class CommandHelperController {

    private final EnumMapper enumMapper;

    @GetMapping("/server")
    @Operation(summary = "Серверные команды")
    public CommandDto[] getServerCommands() {
        return enumMapper.dto(ServerCommand.values());
    }

    @GetMapping("/server/{type}/list")
    @Operation(summary = "Вариации серверной команды")
    public Set<String> getVariations(@PathVariable("type") ServerCommand serverCommand) {
        return serverCommand.getCommands();
    }

    @GetMapping("/client")
    @Operation(summary = "Клиентские команды")
    public CommandDto[] getClientCommands() {
        return enumMapper.dto(ClientCommand.values());
    }

    @GetMapping("/client/{type}/list")
    @Operation(summary = "Вариации клиентской команды")
    public Set<String> getVariations(@PathVariable("type") ClientCommand clientCommand) {
        return clientCommand.getCommands();
    }
}