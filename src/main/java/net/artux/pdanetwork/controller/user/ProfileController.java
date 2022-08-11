package net.artux.pdanetwork.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.achievement.AchievementEntity;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.models.user.Profile;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;
import net.artux.pdanetwork.service.profile.ProfileService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Профиль")
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{id}")
    public Profile getProfile(@PathVariable("id") UUID id) {
        return profileService.getProfile(id);
    }

    @GetMapping
    public Profile getProfile() {
        return profileService.getProfile();
    }

    @GetMapping("/achievements")
    public List<AchievementEntity> getAchievements() {
        return profileService.getAchievements();
    }

    @GetMapping("/achievements/{id}")
    public List<AchievementEntity> getAchievements(@PathVariable("id") UUID id) {
        return profileService.getAchievements(id);
    }

    @Operation(summary = "Рейтинг пользователей")
    @GetMapping("/rating")
    public ResponsePage<SimpleUserDto> getRating(@Valid @ParameterObject QueryPage queryPage) {
        return profileService.getRating(queryPage);
    }
}