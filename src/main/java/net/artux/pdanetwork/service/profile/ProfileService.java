package net.artux.pdanetwork.service.profile;

import net.artux.pdanetwork.models.user.Profile;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.models.user.dto.UserInfoDto;
import net.artux.pdanetwork.models.achievement.AchievementEntity;

import java.util.List;

public interface ProfileService {

    Profile getProfile();

    Profile getProfile(Long pdaId);

    ResponsePage<UserInfoDto> getRating(QueryPage queryPage);

    List<AchievementEntity> getAchievements();

    List<AchievementEntity> getAchievements(Long pdaId);

}
