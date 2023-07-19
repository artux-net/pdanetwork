package net.artux.pdanetwork.service.profile;

import net.artux.pdanetwork.entity.achievement.AchievementEntity;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.models.user.Profile;
import net.artux.pdanetwork.models.user.dto.AdminEditUserDto;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;

import java.util.List;
import java.util.UUID;

public interface ProfileService {

    Profile getProfile();

    Profile getProfile(UUID pdaId);

    ResponsePage<SimpleUserDto> getRating(QueryPage queryPage);

    ResponsePage<SimpleUserDto> getUsersPage(QueryPage queryPage);

    List<AchievementEntity> getAchievements();

    List<AchievementEntity> getAchievements(UUID pdaId);

    ResponsePage<SimpleUserDto> findUsers(String login, QueryPage queryPage);

    ResponsePage<SimpleUserDto> findUsers(AdminEditUserDto exampleDto, QueryPage queryPage);
}
