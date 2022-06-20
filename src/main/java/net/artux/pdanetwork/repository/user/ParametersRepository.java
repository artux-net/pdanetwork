package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.models.profile.story.ParameterEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface ParametersRepository extends JpaRepository<ParameterEntity, Long> {

    List<ParameterEntity> findAllByUser(UserEntity userEntity);
    Optional<ParameterEntity> getParameterEntityByUserAndKey(UserEntity entity, String key);
    void deleteAllByUserAndKey(UserEntity entity, String key);

    boolean existsParameterEntityByUserAndKeyEquals(UserEntity userEntity, String key);


}
