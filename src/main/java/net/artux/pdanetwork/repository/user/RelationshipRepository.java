package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.entity.user.RelationshipEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface RelationshipRepository extends JpaRepository<RelationshipEntity, Long> {

    List<RelationshipEntity> getAllByUser1_IdOrUser2_Id(Long uuid, Long uuid1);

    Optional<RelationshipEntity> getByUser1AndUser2(UserEntity user1, UserEntity user2);

}
