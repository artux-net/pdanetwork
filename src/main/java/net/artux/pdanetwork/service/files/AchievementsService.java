package net.artux.pdanetwork.service.files;

import net.artux.pdanetwork.models.UserAchievementEntity;
import net.artux.pdanetwork.models.achievement.AchievementEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.repository.achievements.AchievementsRepository;
import net.artux.pdanetwork.repository.achievements.UserAchievementsRepository;
import net.artux.pdanetwork.service.CRUDService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementsService extends CRUDService<AchievementEntity> {

    private final UserAchievementsRepository userAchievementsRepository;

    public AchievementsService(AchievementsRepository achievementsRepository, UserAchievementsRepository userAchievementsRepository) {
        super(achievementsRepository);
        this.userAchievementsRepository = userAchievementsRepository;
    }

    public List<AchievementEntity> getForUser(UserEntity entity) {
        return userAchievementsRepository.getAllByUser(entity).stream().map(UserAchievementEntity::getAchievement).collect(Collectors.toList());
    }

}
