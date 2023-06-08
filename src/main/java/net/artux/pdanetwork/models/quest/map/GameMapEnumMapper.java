package net.artux.pdanetwork.models.quest.map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GameMapEnumMapper {

    @Mapping(target = "id", expression = "java(enumGetter.getId())")
    @Mapping(target = "title", expression = "java(enumGetter.getTitle())")
    @Mapping(target = "name", expression = "java(enumGetter.getName())")
    @Mapping(target = "background", expression = "java(enumGetter.getBackground())")
    @Mapping(target = "defaultPosition", expression = "java(enumGetter.getDefaultPosition())")
    GameMapDto dto(MapEnumGetter enumGetter);

    List<GameMapDto> dto(List<MapEnumGetter> enumGetters);

    GameMapDto[] dto(MapEnumGetter[] enumGetters);

}
