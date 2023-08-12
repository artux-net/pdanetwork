package net.artux.pdanetwork.repository.achievement;

import net.artux.pdanetwork.entity.achievement.AchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface AchievementRepository extends JpaRepository<AchievementEntity, String> {

    @Query("select a, (?1 member of a.users) as enabled from AchievementEntity a ")
    List<AchievementEntity> findAll();

}
