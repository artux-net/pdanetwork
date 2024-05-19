package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.entity.user.ParameterEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public interface ParametersRepository extends JpaRepository<ParameterEntity, UUID> {

    List<ParameterEntity> findAllByUser(UserEntity userEntity);

    Optional<ParameterEntity> getParameterEntityByUserAndKey(UserEntity entity, String key);

    void deleteAllByUserAndKey(UserEntity entity, String key);

    void deleteAllByUser(UserEntity entity);

    boolean existsParameterEntityByUserAndKeyEquals(UserEntity userEntity, String key);


}
