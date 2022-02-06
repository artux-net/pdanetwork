package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.profile.NoteEntity;
import net.artux.pdanetwork.service.note.NoteService;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "Заметки")
@RequestMapping("/notes")
public class NoteController {

  private final NoteService noteService;

  @GetMapping
  public List<NoteEntity> getNotes(){
    return noteService.getNotes();
  }

  @PostMapping
  public NoteEntity createNote(@RequestBody String title){
    return noteService.createNote(title);
  }

  @PutMapping
  public NoteEntity editNote(@RequestBody NoteEntity noteEntity){
    return noteService.editNote(noteEntity);
  }

  @DeleteMapping
  public Status deleteNote(@QueryParam("cid") Integer id){
    return noteService.deleteNote(id);
  }

}