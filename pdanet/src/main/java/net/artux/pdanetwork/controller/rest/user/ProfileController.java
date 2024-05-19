package net.artux.pdanetwork.controller.rest.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.feed.PostDto;
import net.artux.pdanetwork.dto.page.QueryPage;
import net.artux.pdanetwork.dto.page.ResponsePage;
import net.artux.pdanetwork.models.user.Profile;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;
import net.artux.pdanetwork.repository.achievement.RepositoryAchCategoryDto;
import net.artux.pdanetwork.service.achievement.AchievementService;
import net.artux.pdanetwork.service.feed.PostService;
import net.artux.pdanetwork.service.profile.ProfileService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Профиль")
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final PostService postService;
    private final AchievementService achievementService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить профиль пользователя по id")
    public Profile getProfile(@PathVariable("id") UUID id) {
        return profileService.getProfile(id);
    }

    @GetMapping
    @Operation(summary = "Получить профиль пользователя")
    public Profile getProfile() {
        return profileService.getProfile();
    }

    @GetMapping("/achievements")
    @Operation(summary = "Получить достижения пользователя")
    public List<RepositoryAchCategoryDto> getAchievements() {
        return achievementService.getUserAchievements();
    }

    @GetMapping("/{id}/achievements")
    @Operation(summary = "Получить достижения пользователя по id")
    public List<RepositoryAchCategoryDto> getAchievements(@PathVariable("id") UUID id) {
        return achievementService.getUserAchievements(id);
    }

    @GetMapping("/{id}/posts")
    @Operation(summary = "Получить достижения пользователя по id")
    public ResponsePage<PostDto> getPosts(@PathVariable UUID id, @Valid QueryPage page) {
        return postService.getUserPosts(id, page);
    }

    @Operation(summary = "Рейтинг пользователей")
    @GetMapping("/rating")
    public ResponsePage<SimpleUserDto> getRating(@Valid @ParameterObject QueryPage queryPage) {
        return profileService.getRating(queryPage);
    }
}