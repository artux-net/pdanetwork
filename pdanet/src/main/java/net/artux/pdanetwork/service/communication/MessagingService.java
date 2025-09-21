package net.artux.pdanetwork.service.communication;

import net.artux.pdanetwork.entity.communication.ConversationEntity;
import net.artux.pdanetwork.entity.communication.MessageEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.communication.ConversationDTO;
import net.artux.pdanetwork.models.communication.MessageDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

public interface MessagingService {

    MessageDTO saveToConversation(String content, ConversationEntity conversationEntity);
    MessageEntity saveMessageToConversation(MessageDTO messageDTO, ConversationDTO conversation, UserEntity user);

    List<MessageDTO> getMessagesByConversationId(UUID conversationId);

    Slice<MessageDTO> getLastMessages(ConversationEntity conversationEntity, PageRequest of);
}
