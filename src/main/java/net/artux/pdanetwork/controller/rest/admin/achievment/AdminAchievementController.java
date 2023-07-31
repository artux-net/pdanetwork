package net.artux.pdanetwork.controller.rest.admin.achievment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.achievement.AchCategoryCreateDto;
import net.artux.pdanetwork.models.achievement.AchCategoryDto;
import net.artux.pdanetwork.models.achievement.AchDto;
import net.artux.pdanetwork.models.achievement.AchievementCreateDto;
import net.artux.pdanetwork.repository.achievement.RepositoryAchCategoryDto;
import net.artux.pdanetwork.service.achievement.AchievementService;
import net.artux.pdanetwork.utills.security.AdminAccess;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Достижения", description = "Доступен с роли админа")
@RequestMapping("/api/v1/admin/achievements")
@RequiredArgsConstructor
@AdminAccess
public class AdminAchievementController {

    private final AchievementService achievementService;

    @GetMapping
    public List<RepositoryAchCategoryDto> getAllAchievements() {
        return achievementService.getAllAchievements();
    }

    @PostMapping("/category")
    @Operation(description = "Создание категории")
    public AchCategoryDto createCategory(@Valid @RequestBody AchCategoryCreateDto createDto) {
        return achievementService.createCategory(createDto);
    }

    @PutMapping("/category/{id}")
    @Operation(description = "Редактирование категории")
    public AchCategoryDto updateCategory(@PathVariable UUID id, @Valid @RequestBody AchCategoryCreateDto createDto) {
        return achievementService.updateCategory(id, createDto);
    }

    @DeleteMapping("/category/{id}")
    @Operation(description = "Удаление категории")
    public boolean deleteCategory(@PathVariable UUID id) {
        return achievementService.deleteCategory(id);
    }

    @PostMapping("/{categoryId}")
    @Operation(description = "Создание достижения в категории")
    public AchDto createAchievement(@Valid @RequestBody AchievementCreateDto createDto, @PathVariable UUID categoryId) {
        return achievementService.createAchievement(categoryId, createDto);
    }

    @PutMapping("/{id}")
    @Operation(description = "Редактирование достижения")
    public AchDto updateAchievement(@PathVariable UUID id, @Valid @RequestBody AchievementCreateDto createDto) {
        return achievementService.updateAchievement(id, createDto);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Удаление достижения")
    public boolean deleteAchievement(@PathVariable UUID id) {
        return achievementService.deleteAchievement(id);
    }

}
