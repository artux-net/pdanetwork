package net.artux.pdanetwork.models.note;

import net.artux.pdanetwork.entity.note.NoteEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring", imports = Instant.class)
public interface NoteMapper {


    @Mapping(target = "time", expression = "java(Instant.now())")
    NoteEntity entity(NoteCreateDto dto, UserEntity author);
    NoteDto to(NoteEntity noteEntity);
    List<NoteDto> list(List<NoteEntity> list);

}
