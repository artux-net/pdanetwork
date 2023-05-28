package net.artux.pdanetwork.models.quest;

import net.artux.pdanetwork.models.quest.admin.StoryInfoAdmin;
import net.artux.pdanetwork.models.quest.map.GameMapDto;
import net.artux.pdanetwork.models.quest.map.MapEnumGetter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestMapper {

    @Mapping(target = "stages", expression = "java(story.stageCount())")
    @Mapping(target = "chapters", expression = "java(story.chapterCount())")
    @Mapping(target = "points", expression = "java(story.pointCount())")
    StoryInfoAdmin adminInfo(StoryDto story);

    List<StoryInfoAdmin> adminInfo(Collection<StoryDto> story);

    StoryInfo info(StoryDto story);

    List<StoryInfo> info(Collection<StoryDto> story);

    @Mapping(target = "id", expression = "java(enumGetter.getId())")
    @Mapping(target = "name", expression = "java(enumGetter.getName())")
    @Mapping(target = "background", expression = "java(enumGetter.getBackground())")
    @Mapping(target = "title", expression = "java(enumGetter.getTitle())")
    GameMapDto dto(MapEnumGetter enumGetter);

    List<GameMapDto> mapsDto(Collection<MapEnumGetter> enumGetters);

    StoryDto dto(Story story);

    List<StoryDto> storiesDto(Collection<Story> story);

    ChapterDto dto(Chapter chapter);

    List<ChapterDto> chaptersDto(Collection<Chapter> chapter);

}
