package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.entity.user.StoryStateEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public interface StoryRepository extends JpaRepository<StoryStateEntity, UUID> {

    Optional<StoryStateEntity> findByUserAndCurrentIsTrue(UserEntity userEntity);

    List<StoryStateEntity> findAllByUser(UserEntity userEntity);

    Optional<StoryStateEntity> findByUserAndStoryId(UserEntity userEntity, int storyId);

    void deleteAllByUser(UserEntity userEntity);
}
