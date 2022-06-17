package net.artux.pdanetwork.service.profile;

import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.ResponsePage;
import net.artux.pdanetwork.models.UserInfo;
import net.artux.pdanetwork.models.achievement.AchievementEntity;

import java.util.List;

public interface ProfileService {

    Profile getProfile();

    Profile getProfile(Long pdaId);

    ResponsePage<UserInfo> getRating(QueryPage queryPage);

    List<AchievementEntity> getAchievements();

    List<AchievementEntity> getAchievements(Long pdaId);

}
