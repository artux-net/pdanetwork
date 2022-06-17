package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.models.profile.ParameterEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public interface ParametersRepository extends JpaRepository<ParameterEntity, Long> {

    List<ParameterEntity> getByUserId(UUID userId);

    Optional<ParameterEntity> getParameterEntityByUserAndKey(UserEntity entity, String key);

    boolean existsParameterEntityByUserAndKeyEquals(UserEntity userEntity, String key);


}
