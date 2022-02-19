package net.artux.pdanetwork.service.profile;

import net.artux.pdanetwork.models.AchievementEntity;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.models.profile.Achievement;

import java.util.List;

public interface ProfileService {

  Profile getProfile();
  Profile getProfile(Integer pdaId);

  List<AchievementEntity> getAchievements();
  List<AchievementEntity> getAchievements(Integer pdaId);

}
