package net.artux.pdanetwork.repository.comminication;

import net.artux.pdanetwork.entity.communication.MessageEntity;
import net.artux.pdanetwork.entity.communication.ConversationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    Slice<MessageEntity> findByConversation(ConversationEntity conversation, Pageable pageable);

}
