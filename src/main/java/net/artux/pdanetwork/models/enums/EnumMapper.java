package net.artux.pdanetwork.models.enums;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EnumMapper {

    @Mapping(target = "id", expression = "java(enumGetter.getId())")
    @Mapping(target = "title", expression = "java(enumGetter.getTitle())")
    EnumDto dto(EnumGetter enumGetter);

    List<EnumDto> dto(List<EnumGetter> enumGetters);

    EnumDto[] dto(EnumGetter[] enumGetters);

}
