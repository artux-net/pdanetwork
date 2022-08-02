package net.artux.pdanetwork.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.models.user.dto.UserInfoDto;
import net.artux.pdanetwork.entity.achievement.AchievementEntity;
import net.artux.pdanetwork.models.user.Profile;
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
  public ResponsePage<UserInfoDto> getRating(@Valid QueryPage queryPage){
    return profileService.getRating(queryPage);
  }
}