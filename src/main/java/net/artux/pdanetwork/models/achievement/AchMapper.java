package net.artux.pdanetwork.models.achievement;

import net.artux.pdanetwork.entity.achievement.AchievementCategoryEntity;
import net.artux.pdanetwork.entity.achievement.AchievementEntity;
import net.artux.pdanetwork.models.user.UserMapper;
import net.artux.pdanetwork.service.util.ValuesService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ValuesService.class, UserMapper.class})
public interface AchMapper {

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "image", source = "dto.image")
    @Mapping(target = "title", source = "dto.title")
    AchievementEntity toEntity(AchievementCreateDto dto, AchievementCategoryEntity category);

    @Mapping(target = "achievements", ignore = true)
    AchievementCategoryEntity toEntity(AchCategoryCreateDto createDto);

    AchCategoryDto dto(AchievementCategoryEntity entity);

    AchDto dto(AchievementEntity entity);
}
