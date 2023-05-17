package net.artux.pdanetwork.models.quest;

import net.artux.pdanetwork.models.quest.admin.StoryInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestMapper {

    StoryDto dto(Story story);
    List<StoryDto> dto(Collection<Story> story);

    @Mapping(target = "stages", expression = "java(story.stageCount())")
    @Mapping(target = "chapters", expression = "java(story.chapterCount())")
    @Mapping(target = "points", expression = "java(story.pointCount())")
    StoryInfo info(Story story);
    List<StoryInfo> info(Collection<Story> story);

}
