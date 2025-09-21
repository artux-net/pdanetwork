package net.artux.pdanetwork.service.note;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.note.NoteCreateDto;
import net.artux.pdanetwork.models.note.NoteDto;

import java.util.List;
import java.util.UUID;

public interface NoteService {

    List<NoteDto> getNotes();

    NoteDto createNote(NoteCreateDto dto);

    Status deleteNote(UUID id);

    NoteDto editNote(UUID id, NoteCreateDto dto);
}
