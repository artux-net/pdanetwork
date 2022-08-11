package net.artux.pdanetwork.repository.achievements;

import net.artux.pdanetwork.entity.achievement.AchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface AchievementsRepository extends JpaRepository<AchievementEntity, UUID> {

}
