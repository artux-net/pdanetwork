package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.entity.conversation.ConversationEntity;
import net.artux.pdanetwork.entity.items.ArmorEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {

    @Query(value = "select c from ConversationEntity c where ?1 in c.members and " +
            " c.type = ?3")
    Optional<ConversationEntity> findByMembersContainsAndType(ConversationEntity.Type type, long... pda1);

    @Query(value = "select c from ConversationEntity c where ?1 in c.members")
    Slice<ConversationEntity> findByMembersContains(Pageable pageable, long... pda1);

    Optional<ConversationEntity> findByIdAndMembersContains(long id, UserEntity entity);
    Optional<ConversationEntity> findByIdAndOwner(long id, UserEntity entity);
}
