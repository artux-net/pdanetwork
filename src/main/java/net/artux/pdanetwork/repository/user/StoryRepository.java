package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.entity.user.StoryStateEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public interface StoryRepository extends JpaRepository<StoryStateEntity, UUID> {

    @Query(value = "select s from StoryStateEntity s where s.current = true and s.user.id = ?#{principal.id}")
    Optional<StoryStateEntity> findByUserAndCurrentIsTrue();

    List<StoryStateEntity> findAllByUser(UserEntity userEntity);

    @Query(value = "select s from StoryStateEntity s where s.id = ?1 and s.user.id = ?#{principal.id}")
    Optional<StoryStateEntity> findByUserAndStoryId(int storyId);

    void deleteAllByUser(UserEntity userEntity);
}
