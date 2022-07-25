package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.entity.MessageEntity;
import net.artux.pdanetwork.entity.conversation.ConversationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    Slice<MessageEntity> findByConversation(ConversationEntity conversation, Pageable pageable);

}
