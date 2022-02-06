package net.artux.pdanetwork.service.note;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.profile.NoteEntity;

import java.util.List;

public interface NoteService {

  List<NoteEntity> getNotes();
  NoteEntity createNote(String content);
  NoteEntity editNote(NoteEntity noteEntity);
  Status deleteNote(Integer id);

}
