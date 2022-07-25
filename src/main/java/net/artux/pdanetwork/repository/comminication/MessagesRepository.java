package net.artux.pdanetwork.repository.comminication;

import net.artux.pdanetwork.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface MessagesRepository extends JpaRepository<MessageEntity, Long> {

}
