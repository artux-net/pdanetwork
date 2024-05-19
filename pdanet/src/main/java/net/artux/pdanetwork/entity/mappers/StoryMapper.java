package net.artux.pdanetwork.entity.mappers;

import net.artux.pdanetwork.entity.user.ParameterEntity;
import net.artux.pdanetwork.entity.user.StoryStateEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.entity.user.gang.GangRelationEntity;
import net.artux.pdanetwork.models.story.ParameterDto;
import net.artux.pdanetwork.models.story.StoryStateDto;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.models.user.gang.GangRelationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface StoryMapper {

    ParameterDto param(ParameterEntity parameterEntity);

    List<ParameterDto> params(List<ParameterEntity> parameterEntities);

    StoryStateDto state(StoryStateEntity storyStateEntity);

    List<StoryStateDto> states(List<StoryStateEntity> storyStateEntities);

    @Mapping(target = "relations", expression = "java(mapRelation(user.getGangRelation()))")
    @Mapping(target = "money", expression = "java(user.getMoney())")
    StoryData storyData(UserEntity user);

    GangRelationDto mapRelation(GangRelationEntity value);

}
