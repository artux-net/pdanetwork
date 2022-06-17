package net.artux.pdanetwork.service.note;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.note.NoteDto;
import net.artux.pdanetwork.models.note.NoteEntity;
import net.artux.pdanetwork.models.note.NoteMapper;

import java.util.List;

public interface NoteService {

  List<NoteDto> getNotes();
  NoteDto createNote(String content);
  NoteDto createNote(String title, String content);
  NoteDto editNote(NoteDto noteEntity);
  Status deleteNote(Long id);

}
