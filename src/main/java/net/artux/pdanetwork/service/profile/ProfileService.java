package net.artux.pdanetwork.service.profile;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.models.profile.Achievement;

import java.util.List;

public interface ProfileService {

  Profile getProfile();
  Profile getProfile(Integer pdaId);

  List<Achievement> getAchievements();
  List<Achievement> getAchievements(Integer pdaId);

}
