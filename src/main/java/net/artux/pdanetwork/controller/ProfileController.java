package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.AchievementEntity;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.models.profile.Achievement;
import net.artux.pdanetwork.service.profile.ProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "Профиль")
@RequestMapping("/profile")
public class ProfileController {

  private final ProfileService profileService;

  @GetMapping("/{id}")
  public Profile getProfile(@PathVariable("id") Integer id){
    return profileService.getProfile(id);
  }

  @GetMapping
  public Profile getProfile(){
    return profileService.getProfile();
  }

  @GetMapping("/achievements")
  public List<AchievementEntity> getAchievements(){
    return profileService.getAchievements();
  }

  @GetMapping("/achievements/{id}")
  public List<AchievementEntity> getAchievements(@PathVariable("id") Integer id){
    return profileService.getAchievements(id);
  }
}