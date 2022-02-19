package net.artux.pdanetwork.repository.comminication;

import net.artux.pdanetwork.models.communication.MessageEntity;
import net.artux.pdanetwork.models.user.FriendRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface MessagesRepository extends JpaRepository<MessageEntity, UUID> {

    List<MessageEntity> getAllBy(UUID uuid);

}
