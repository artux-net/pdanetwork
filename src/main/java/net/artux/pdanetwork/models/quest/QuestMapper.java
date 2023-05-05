package net.artux.pdanetwork.models.quest;

import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestMapper {

    StoryDto dto(Story story);

    List<StoryDto> dto(Collection<Story> story);

}
