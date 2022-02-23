package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.models.profile.ParameterEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public interface ParametersRepository extends JpaRepository<ParameterEntity, UUID> {

    List<ParameterEntity> getByUserId(UUID userId);
    Optional<ParameterEntity> getParameterEntityByUserIdAndAndKey(UUID uuid, String key);
    boolean existsParameterEntityByUserIdEqualsAndKeyEquals(UUID userId, String key);


}
