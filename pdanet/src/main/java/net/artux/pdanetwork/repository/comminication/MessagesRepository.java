package net.artux.pdanetwork.repository.comminication;

import net.artux.pdanetwork.entity.communication.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface MessagesRepository extends JpaRepository<MessageEntity, UUID> {

}
