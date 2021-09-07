package net.artux.pdanetwork.service.note;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.profile.Note;

import java.util.List;

public interface NoteService {

  List<Note> getNotes();
  Note createNote(String content);
  Note editNote(Note note);
  Status deleteNote(Integer id);

}
