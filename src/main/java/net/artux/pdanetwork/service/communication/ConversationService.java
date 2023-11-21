package net.artux.pdanetwork.service.communication;

import net.artux.pdanetwork.models.communication.ConversationCreateDTO;
import net.artux.pdanetwork.models.communication.ConversationDTO;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ConversationService {

    ConversationDTO createConversation(ConversationCreateDTO createDTO);

    ConversationDTO editConversation(UUID id, ConversationCreateDTO createDTO);

    ConversationDTO getConversation(UUID id);
    List<ConversationDTO> getConversationWithUser(UUID userId);

    Page<ConversationDTO> getConversations(Pageable queryPage);

    ResponsePage<ConversationDTO> getConversations(QueryPage queryPage);

    boolean deleteConversation(UUID id);

    ConversationDTO getConversationEntity(UUID id);
}
