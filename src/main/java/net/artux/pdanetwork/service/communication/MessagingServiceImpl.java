package net.artux.pdanetwork.service.communication;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.MessageEntity;
import net.artux.pdanetwork.entity.conversation.ConversationEntity;
import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.models.communication.MessageMapper;
import net.artux.pdanetwork.repository.items.MessageRepository;
import net.artux.pdanetwork.service.member.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MessagingServiceImpl implements MessagingService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final MessageMapper messageMapper;

    @Override
    public MessageDTO saveToConversation(String content, ConversationEntity conversationEntity) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setAuthor(userService.getMember());
        messageEntity.setContent(content);
        messageEntity.setTimestamp(Instant.now());
        return messageMapper.dto(messageRepository.save(messageEntity));
    }

    @Override
    public Slice<MessageDTO> getLastMessages(ConversationEntity conversationEntity, PageRequest of) {
        return messageRepository.findByConversation(conversationEntity, of).map(messageMapper::dto);
    }
}
