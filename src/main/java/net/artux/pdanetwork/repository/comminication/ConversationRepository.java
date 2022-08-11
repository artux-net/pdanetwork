package net.artux.pdanetwork.repository.comminication;

import net.artux.pdanetwork.entity.communication.ConversationEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface ConversationRepository extends JpaRepository<ConversationEntity, UUID> {

    @Query(value = "select c from ConversationEntity c where ?2 in c.members and " +
            " c.type = ?1")
    Optional<ConversationEntity> findByMembersContainsAndType(ConversationEntity.Type type, UUID... pda1);

    @Query(value = "select c from ConversationEntity c where ?1 in c.members")
    Slice<ConversationEntity> findByMembersContains(Pageable pageable, UUID... pda1);

    Optional<ConversationEntity> findByIdAndMembersContains(UUID id, UserEntity entity);

    Optional<ConversationEntity> findByIdAndOwner(UUID id, UserEntity entity);
}
