package net.artux.pdanetwork.controller.communication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.communication.ConversationCreateDTO;
import net.artux.pdanetwork.models.communication.ConversationDTO;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.service.communication.ConversationService;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Беседы")
@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping
    @Operation(summary = "Все беседы с участием пользователя")
    public Slice<ConversationDTO> getConversations(QueryPage queryPage){
        return conversationService.getConversations(queryPage);
    }

    @PostMapping("/create")
    @Operation(summary = "Создание беседы")
    public ConversationDTO createConversation(ConversationCreateDTO createDTO) {
        return conversationService.createConversation(createDTO);
    }

    @PostMapping("/{id}/edit")
    @Operation(summary = "Изменение беседы")
    public ConversationDTO editConversation(@PathVariable UUID id, ConversationCreateDTO createDTO) {
        return conversationService.editConversation(id, createDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Информация о беседе")
    public ConversationDTO getConversation(@PathVariable UUID id) {
        return conversationService.getConversation(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление беседы")
    public boolean deleteConversation(@PathVariable UUID id) {
        return conversationService.deleteConversation(id);
    }

}
