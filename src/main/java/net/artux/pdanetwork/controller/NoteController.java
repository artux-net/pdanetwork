package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.profile.Note;
import net.artux.pdanetwork.service.note.NoteService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "Заметки")
@RequestMapping("/notes")
public class NoteController {

  private final NoteService noteService;

  @GetMapping
  public List<Note> getNotes(){
    return noteService.getNotes();
  }

  @PostMapping
  public Note createNote(@RequestBody String title){
    return noteService.createNote(title);
  }

  @PutMapping
  public Note editNote(@RequestBody Note note){
    return noteService.editNote(note);
  }

  @DeleteMapping
  public Status deleteNote(@QueryParam("cid") Integer id){
    return noteService.deleteNote(id);
  }

}