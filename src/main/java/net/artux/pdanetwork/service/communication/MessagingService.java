package net.artux.pdanetwork.service.communication;

import net.artux.pdanetwork.entity.conversation.ConversationEntity;
import net.artux.pdanetwork.models.communication.MessageDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

public interface MessagingService {

    MessageDTO saveToConversation(String content, ConversationEntity conversationEntity);

    Slice<MessageDTO> getLastMessages(ConversationEntity conversationEntity, PageRequest of);
}
