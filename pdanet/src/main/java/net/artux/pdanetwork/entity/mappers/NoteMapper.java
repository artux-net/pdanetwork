package net.artux.pdanetwork.entity.mappers;

import net.artux.pdanetwork.entity.note.NoteEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.note.NoteCreateDto;
import net.artux.pdanetwork.models.note.NoteDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring", imports = Instant.class)
public interface NoteMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", source = "author")
    @Mapping(target = "time", expression = "java(Instant.now())")
    NoteEntity entity(NoteCreateDto dto, UserEntity author);
    NoteDto to(NoteEntity noteEntity);
    List<NoteDto> list(List<NoteEntity> list);

}
