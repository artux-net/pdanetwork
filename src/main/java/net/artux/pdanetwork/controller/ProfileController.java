package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.ResponsePage;
import net.artux.pdanetwork.models.profile.Achievement;
import net.artux.pdanetwork.service.feed.FeedService;
import net.artux.pdanetwork.service.profile.ProfileService;
import net.artux.pdanetwork.servlets.Feed.Models.Article;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Api(tags = "Профили")
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
  public List<Achievement> getAchievements(){
    return profileService.getAchievements();
  }

  @GetMapping("/achievements/{id}")
  public List<Achievement> getAchievements(@PathVariable("id") Integer id){
    return profileService.getAchievements(id);
  }
}