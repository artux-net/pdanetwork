package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.entity.user.relationship.RelationshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface RelationshipRepository extends JpaRepository<RelationshipEntity, UUID> {

    Optional<RelationshipEntity> getByUser1AndUser2(UserEntity user1, UserEntity user2);

}
