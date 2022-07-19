package net.artux.pdanetwork.models.story;

import net.artux.pdanetwork.entity.ParameterEntity;
import net.artux.pdanetwork.entity.StoryStateEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoryMapper {

    ParameterDto param(ParameterEntity parameterEntity);

    List<ParameterDto> params(List<ParameterEntity> parameterEntities);

    StoryStateDto state(StoryStateEntity storyStateEntity);

    List<StoryStateDto> states(List<StoryStateEntity> storyStateEntities);

}
