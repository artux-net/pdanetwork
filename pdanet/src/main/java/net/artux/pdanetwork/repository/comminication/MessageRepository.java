package net.artux.pdanetwork.repository.comminication;

import net.artux.pdanetwork.entity.communication.ConversationEntity;
import net.artux.pdanetwork.entity.communication.MessageEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {
    Slice<MessageEntity> findByConversation(ConversationEntity conversation, Pageable pageable);
    List<MessageEntity> findAllByConversation(ConversationEntity conversation);
    void deleteAllByConversation(ConversationEntity conversation);
    long deleteAllByAuthor(UserEntity user);
}
