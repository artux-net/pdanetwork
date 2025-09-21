package net.artux.pdanetwork.entity.mappers;

import net.artux.pdanetwork.models.command.ClientCommand;
import net.artux.pdanetwork.models.enums.EnumGetter;
import net.artux.pdanetwork.models.command.ServerCommand;
import net.artux.pdanetwork.models.enums.CommandDto;
import net.artux.pdanetwork.models.enums.EnumDto;
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

    CommandDto dto(ServerCommand serverCommand);
    CommandDto[] dto(ServerCommand[] serverCommands);

    CommandDto dto(ClientCommand clientCommand);
    CommandDto[] dto(ClientCommand[] clientCommands);

}
