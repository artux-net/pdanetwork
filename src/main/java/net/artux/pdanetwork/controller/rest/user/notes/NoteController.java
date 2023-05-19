package net.artux.pdanetwork.controller.rest.user.notes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.note.NoteCreateDto;
import net.artux.pdanetwork.models.note.NoteDto;
import net.artux.pdanetwork.service.note.NoteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Заметки")
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    @Operation(summary = "Получить заметки")
    public List<NoteDto> getNotes() {
        return noteService.getNotes();
    }

    @PostMapping
    @Operation(summary = "Создать заметку")
    public NoteDto createNote(@RequestBody NoteCreateDto dto) {
        return noteService.createNote(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить заметку")
    public NoteDto editNote(@PathVariable UUID id, @RequestBody NoteCreateDto dto) {
        return noteService.editNote(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заметку")
    public Status deleteNote(@PathVariable UUID id) {
        return noteService.deleteNote(id);
    }

}