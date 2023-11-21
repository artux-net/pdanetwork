package net.artux.pdanetwork.repository.comminication;

import net.artux.pdanetwork.entity.communication.ConversationEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<ConversationEntity, UUID> {

   /* @Query(value = "select c from ConversationEntity c where ?2 in c.members and c.type = ?1")
    Optional<ConversationEntity> findByMembersContainsAndType(ConversationEntity.Type type, UUID... pda1);

    @Query(value = "select c from ConversationEntity c where :ids in c.members")
    Slice<ConversationEntity> findByMembersContains(UUID ids, Pageable pageable);*/

    Page<ConversationEntity> findAllByMembersContains(UserEntity user, Pageable pageable);

    List<ConversationEntity> findAllByTypeAndMembers_Id(ConversationEntity.Type type, UUID members_id);

    Optional<ConversationEntity> findByIdAndMembersContains(UUID id, UserEntity entity);

    Optional<ConversationEntity> findByIdAndOwner(UUID id, UserEntity entity);
}
