package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.entity.user.gang.GangRelationEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GangRelationsRepository extends JpaRepository<GangRelationEntity, Long> {

    Optional<GangRelationEntity> findByUser(UserEntity entity);

}
