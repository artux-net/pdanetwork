package net.artux.pdanetwork.service.communication;

import net.artux.pdanetwork.entity.conversation.ConversationEntity;
import net.artux.pdanetwork.models.communication.ConversationCreateDTO;
import net.artux.pdanetwork.models.communication.ConversationDTO;
import net.artux.pdanetwork.models.user.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ConversationService {

    ConversationDTO createConversation(ConversationCreateDTO createDTO);

    ConversationDTO editConversation(Long id, ConversationCreateDTO createDTO);

    ConversationDTO getConversation(Long id);

    Slice<ConversationDTO> getConversations(Pageable queryPage);

    boolean deleteConversation(Long id);

    ConversationEntity getPrivateConversation(Long pda1, long pda2);

    ConversationEntity getConversationByIdForUser(Long id, UserEntity user);

    ConversationEntity createPrivateConversation(long pdaId, long id);
}
