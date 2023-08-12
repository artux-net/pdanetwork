package net.artux.pdanetwork.service.achievement;

import net.artux.pdanetwork.models.achievement.AchCategoryCreateDto;
import net.artux.pdanetwork.models.achievement.AchCategoryDto;
import net.artux.pdanetwork.models.achievement.AchDto;
import net.artux.pdanetwork.models.achievement.AchievementCreateDto;
import net.artux.pdanetwork.repository.achievement.RepositoryAchCategoryDto;

import java.util.List;
import java.util.UUID;

public interface AchievementService {

    List<RepositoryAchCategoryDto> getUserAchievements(UUID userId);

    List<RepositoryAchCategoryDto> getUserAchievements();

    List<RepositoryAchCategoryDto> getAllAchievements();

    AchCategoryDto createCategory(AchCategoryCreateDto createDto);

    AchCategoryDto updateCategory(String name, AchCategoryCreateDto createDto);

    boolean deleteCategory(String name);

    AchDto createAchievement(String name, AchievementCreateDto createDto);

    AchDto updateAchievement(String name, AchievementCreateDto createDto);

    boolean deleteAchievement(String name);

}
