package net.artux.pdanetwork.models.story;

import net.artux.pdanetwork.entity.user.ParameterEntity;
import net.artux.pdanetwork.entity.user.StoryStateEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.items.ItemMapper;
import net.artux.pdanetwork.models.user.UserMapper;
import net.artux.pdanetwork.models.user.dto.StoryData;
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
    StoryData storyData(UserEntity entity);

}
