package net.artux.pdanetwork.controller.communication;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.communication.ConversationCreateDTO;
import net.artux.pdanetwork.models.communication.ConversationDTO;
import net.artux.pdanetwork.service.communication.ConversationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping("/create")
    public ConversationDTO createConversation(ConversationCreateDTO createDTO) {
        return conversationService.createConversation(createDTO);
    }

    @PostMapping("/{id}/edit")
    public ConversationDTO editConversation(@PathVariable long id, ConversationCreateDTO createDTO) {
        return conversationService.editConversation(id, createDTO);
    }

    @GetMapping("/{id}")
    public ConversationDTO getConversation(@PathVariable Long id) {
        return conversationService.getConversation(id);
    }

    @DeleteMapping("/{id}")
    public boolean deleteConversation(@PathVariable Long id) {
        return conversationService.deleteConversation(id);
    }

}
