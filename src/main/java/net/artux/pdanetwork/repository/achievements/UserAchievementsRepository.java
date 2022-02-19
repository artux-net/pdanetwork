package net.artux.pdanetwork.repository.achievements;

import net.artux.pdanetwork.models.UserAchievementEntity;
import net.artux.pdanetwork.models.profile.Achievement;
import net.artux.pdanetwork.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public interface UserAchievementsRepository extends JpaRepository<UserAchievementEntity, UUID> {

    List<UserAchievementEntity> getAllByUser(UserEntity user);
}
