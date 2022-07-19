package net.artux.pdanetwork.repository.achievements;

import net.artux.pdanetwork.entity.AchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface AchievementsRepository extends JpaRepository<AchievementEntity, Long> {

}
