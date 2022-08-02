package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.entity.user.StoryStateEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface StoryRepository extends JpaRepository<StoryStateEntity, Long> {

    Optional<StoryStateEntity> findByPlayerAndCurrentIsTrue(UserEntity userEntity);

    List<StoryStateEntity> findAllByPlayer(UserEntity userEntity);

    Optional<StoryStateEntity> findByPlayerAndStoryId(UserEntity userEntity, int storyId);

    void deleteAllByPlayer(UserEntity userEntity);
}
