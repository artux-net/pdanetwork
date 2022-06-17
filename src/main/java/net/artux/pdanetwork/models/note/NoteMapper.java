package net.artux.pdanetwork.models.note;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    NoteDto to(NoteEntity noteEntity);
    List<NoteDto> list(List<NoteEntity> list);

}
