package net.artux.pdanetwork.repository.achievement;

import net.artux.pdanetwork.entity.achievement.AchievementCategoryEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AchievementCategoryRepository extends JpaRepository<AchievementCategoryEntity, String> {

    @Query("select c,  (?1 member of a.users) as enabled from AchievementCategoryEntity c join AchievementEntity a on a.category = c")
    List<RepositoryAchCategoryDto> findAllByUser(UserEntity user);

    @Query("select c,  (false) as enabled from AchievementCategoryEntity c join AchievementEntity a on a.category = c")
    List<RepositoryAchCategoryDto> findAllWithItems();

}
