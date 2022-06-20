package net.artux.pdanetwork.models.profile.story;

import net.artux.pdanetwork.models.profile.story.dto.ParameterDto;
import net.artux.pdanetwork.models.profile.story.dto.StoryStateDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoryMapper {

    ParameterDto param(ParameterEntity parameterEntity);

    List<ParameterDto> params(List<ParameterEntity> parameterEntities);

    StoryStateDto state(StoryStateEntity storyStateEntity);

    List<StoryStateDto> states(List<StoryStateEntity> storyStateEntities);

}
