package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.note.NoteDto;
import net.artux.pdanetwork.models.note.NoteEntity;
import net.artux.pdanetwork.service.note.NoteService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "Заметки")
@RequestMapping("/notes")
public class NoteController {

  private final NoteService noteService;

  @GetMapping
  public List<NoteDto> getNotes(){
    return noteService.getNotes();
  }

  @PostMapping
  public NoteDto createNote(@RequestBody String title){
    return noteService.createNote(title);
  }

  @PutMapping
  public NoteDto editNote(@RequestBody NoteDto noteEntity){
    return noteService.editNote(noteEntity);
  }

  @DeleteMapping
  public Status deleteNote(@RequestParam("id") Long id){
    return noteService.deleteNote(id);
  }

}