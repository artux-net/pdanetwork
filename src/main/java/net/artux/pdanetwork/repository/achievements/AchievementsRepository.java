package net.artux.pdanetwork.repository.achievements;

import net.artux.pdanetwork.models.AchievementEntity;
import net.artux.pdanetwork.models.communication.GroupEntity;
import net.artux.pdanetwork.models.profile.Achievement;
import net.artux.pdanetwork.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface AchievementsRepository extends JpaRepository<AchievementEntity, UUID> {

}
