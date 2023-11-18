package net.artux.pdanetwork.service.communication;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.communication.ConversationEntity;
import net.artux.pdanetwork.entity.communication.MessageEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.communication.ConversationDTO;
import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.models.communication.MessageMapper;
import net.artux.pdanetwork.repository.comminication.MessageRepository;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MessagingServiceImpl implements MessagingService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final MessageMapper messageMapper;

    @Override
    public MessageDTO saveToConversation(String content, ConversationEntity conversationEntity) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setAuthor(userService.getUserById());
        messageEntity.setContent(content);
        messageEntity.setTimestamp(Instant.now());
        return messageMapper.dto(messageRepository.save(messageEntity));
    }

    @Override
    public MessageEntity saveMessageToConversation(MessageDTO messageDTO, ConversationDTO conversation, UserEntity user) {
        MessageEntity messageEntity = messageMapper.entity(messageDTO);
        ConversationEntity entity = new ConversationEntity();
        entity.setId(conversation.getId());
        messageEntity.setConversation(entity);
        messageEntity.setAuthor(user);
        return messageRepository.save(messageEntity);
    }

    @Override
    public List<MessageDTO> getMessagesByConversationId(UUID conversationId) {
        ConversationEntity conversation = new ConversationEntity();
        conversation.setId(conversationId);
        return messageMapper.list(messageRepository.findAllByConversation(conversation));
    }

    @Override
    public Slice<MessageDTO> getLastMessages(ConversationEntity conversationEntity, PageRequest of) {
        return messageRepository.findByConversation(conversationEntity, of).map(messageMapper::dto);
    }
}
