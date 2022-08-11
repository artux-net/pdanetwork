package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.entity.user.gang.GangRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GangRelationsRepository extends JpaRepository<GangRelationEntity, UUID> {

    Optional<GangRelationEntity> findByUser(UserEntity entity);

}
