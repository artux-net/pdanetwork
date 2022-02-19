package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.models.user.UserEntity;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public interface UsersRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> getUserEntityByUid(ObjectId objectId);
    Optional<UserEntity> getMemberByPdaId(int pdaId);
    Optional<UserEntity> getMemberByLogin(String login);
    Optional<UserEntity> getMemberByEmail(String email);
    Optional<UserEntity> findTopByOrderByPdaIdDesc();
    List<UserEntity> findAllByUidIn(List<UUID> list);


}
