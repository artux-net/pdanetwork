package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.ResponsePage;
import net.artux.pdanetwork.models.UserInfo;
import net.artux.pdanetwork.models.achievement.AchievementEntity;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.service.profile.ProfileService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "Профиль")
@RequestMapping("/profile")
public class ProfileController {

  private final ProfileService profileService;

  @GetMapping("/{id}")
  public Profile getProfile(@PathVariable("id") Long id){
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
  public List<AchievementEntity> getAchievements(@PathVariable("id") Long id){
    return profileService.getAchievements(id);
  }

  @ApiOperation(value = "Рейтинг пользователей")
  @GetMapping("/rating")
  public ResponsePage<UserInfo> getRating(@Valid QueryPage queryPage){
    return profileService.getRating(queryPage);
  }
}