package net.artux.pdanetwork.service.note;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.profile.Note;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface NoteService {

  List<Note> getNotes();
  Note createNote(String content);
  Note editNote(Note note);
  Status deleteNote(Integer id);

}
