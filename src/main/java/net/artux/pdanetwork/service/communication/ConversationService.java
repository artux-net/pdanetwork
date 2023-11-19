package net.artux.pdanetwork.service.communication;

import net.artux.pdanetwork.entity.communication.ConversationEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.communication.ConversationCreateDTO;
import net.artux.pdanetwork.models.communication.ConversationDTO;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ConversationService {

    ConversationDTO createConversation(ConversationCreateDTO createDTO);

    ConversationDTO editConversation(UUID id, ConversationCreateDTO createDTO);

    ConversationDTO getConversation(UUID id);

    Page<ConversationDTO> getConversations(Pageable queryPage);

    ResponsePage<ConversationDTO> getConversations(QueryPage queryPage);

    boolean deleteConversation(UUID id);

    ConversationEntity getPrivateConversation(UUID pda1, UUID pda2);
    ConversationDTO getConversationEntity(UUID id);

    ConversationEntity getConversationByIdForUser(UUID id, UserEntity user);

    ConversationEntity createPrivateConversation(UUID pdaId, UUID id);
}
