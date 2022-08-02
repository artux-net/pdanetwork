package net.artux.pdanetwork.repository.achievements;

import net.artux.pdanetwork.entity.achievement.UserAchievementEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface UserAchievementsRepository extends JpaRepository<UserAchievementEntity, UUID> {

    List<UserAchievementEntity> getAllByUser(UserEntity user);
}
