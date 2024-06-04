package net.artux.pdanetwork.service.note;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.note.NoteEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.note.NoteCreateDto;
import net.artux.pdanetwork.models.note.NoteDto;
import net.artux.pdanetwork.entity.mappers.NoteMapper;
import net.artux.pdanetwork.repository.user.NoteRepository;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final UserService userService;
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    @Override
    public List<NoteDto> getNotes() {
        return noteMapper.list(noteRepository.findAllByAuthor(userService.getUserById()));
    }


    @Override
    public NoteDto createNote(NoteCreateDto dto) {
        return noteMapper.to(noteRepository.save(
                noteMapper.entity(dto, userService.getUserById())));
    }

    @Override
    public NoteDto editNote(UUID id, NoteCreateDto note) {
        NoteEntity noteEntity = noteRepository.findById(id).orElseThrow();
        if (!noteEntity.getAuthor().getId().equals(userService.getCurrentId())){
            throw new IllegalArgumentException("You don't have permission to edit this note");
        }
        noteEntity.setContent(note.getContent());
        noteEntity.setTitle(note.getTitle());

        return noteMapper.to(noteRepository.save(noteEntity));
    }

    @Override
    public Status deleteNote(UUID id) {
        NoteEntity noteEntity = noteRepository.findById(id).orElseThrow();
        if (noteEntity.getAuthor().getId() != userService.getCurrentId()){
            throw new IllegalArgumentException("You don't have permission to edit this note");
        }
        noteRepository.delete(noteEntity);
        return new Status(true);
    }

}
