package net.artux.pdanetwork.service.achievement;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.achievement.AchievementCategoryEntity;
import net.artux.pdanetwork.entity.achievement.AchievementEntity;
import net.artux.pdanetwork.entity.achievement.AchievementGroup;
import net.artux.pdanetwork.models.achievement.AchCategoryCreateDto;
import net.artux.pdanetwork.models.achievement.AchCategoryDto;
import net.artux.pdanetwork.models.achievement.AchDto;
import net.artux.pdanetwork.entity.mappers.AchMapper;
import net.artux.pdanetwork.models.achievement.AchievementCreateDto;
import net.artux.pdanetwork.repository.achievement.AchievementCategoryRepository;
import net.artux.pdanetwork.repository.achievement.AchievementRepository;
import net.artux.pdanetwork.repository.achievement.RepositoryAchCategoryDto;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.utills.security.ModeratorAccess;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {

    private final AchievementCategoryRepository categoryRepository;
    private final AchievementRepository achievementRepository;
    private final UserService userService;
    private final AchMapper mapper;

    @Override
    public List<RepositoryAchCategoryDto> getUserAchievements(UUID userId) {
        return categoryRepository.findAllByUser(userService.getUserById(userId));
    }

    @Override
    public List<RepositoryAchCategoryDto> getUserAchievements() {
        return getUserAchievements(userService.getCurrentId());
    }

    @Override
    public List<RepositoryAchCategoryDto> getAllAchievements() {
        return categoryRepository.findAllWithItems();
    }

    @Override
    @ModeratorAccess
    public AchCategoryDto createCategory(AchCategoryCreateDto createDto) {
        AchievementCategoryEntity category = mapper.toEntity(createDto);
        var t = categoryRepository.save(category);
        return mapper.dto(t);
    }

    @Override
    @ModeratorAccess
    public AchCategoryDto updateCategory(UUID id, AchCategoryCreateDto createDto) {
        AchievementCategoryEntity category = categoryRepository.findById(id).orElseThrow();
        category.setName(createDto.name());
        category.setDescription(createDto.description());
        category.setImage(createDto.image());
        category.setTitle(createDto.title());

        return mapper.dto(categoryRepository.save(category));
    }

    @Override
    @ModeratorAccess
    public boolean deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
        return true;
    }

    @Override
    @ModeratorAccess
    public AchDto createAchievement(UUID categoryId, AchievementCreateDto createDto) {
        AchievementCategoryEntity category = categoryRepository.findById(categoryId).orElseThrow();
        AchievementEntity achievementEntity = mapper.toEntity(createDto, category);

        return mapper.dto(achievementRepository.save(achievementEntity));
    }

    @Override
    @ModeratorAccess
    public AchDto updateAchievement(UUID id, AchievementCreateDto createDto) {
        AchievementEntity entity = achievementRepository.findById(id).orElseThrow();
        entity.setCondition(createDto.condition());
        entity.setName(createDto.name());
        entity.setTitle(createDto.title());
        entity.setImage(createDto.image());
        entity.setDescription(createDto.description());
        entity.setType(AchievementGroup.valueOf(createDto.type().name()));

        return mapper.dto(entity);
    }

    @Override
    @ModeratorAccess
    public boolean deleteAchievement(UUID id) {
        achievementRepository.deleteById(id);
        return true;
    }
}
