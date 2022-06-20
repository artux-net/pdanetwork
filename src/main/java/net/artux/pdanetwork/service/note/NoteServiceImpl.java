package net.artux.pdanetwork.service.note;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.note.NoteDto;
import net.artux.pdanetwork.models.note.NoteMapper;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.models.*;
import net.artux.pdanetwork.models.note.NoteEntity;
import net.artux.pdanetwork.repository.user.NoteRepository;
import net.artux.pdanetwork.service.member.UserService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final UserService userService;
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    @Override
    public List<NoteDto> getNotes() {
        return noteMapper.list(noteRepository.findAllByAuthor(userService.getMember()));
    }

    @Override
    public NoteDto createNote(String title) {
        return createNote(title, "");
    }

    @Override
    public NoteDto createNote(String title, String content) {
        UserEntity userEntity = userService.getMember();
        NoteEntity noteEntity = new NoteEntity(title, content);
        noteEntity.setAuthor(userEntity);
        return noteMapper.to(noteRepository.save(noteEntity));
    }

    @Override
    public NoteDto editNote(NoteDto note) {
        NoteEntity noteEntity = noteRepository.findByAuthorAndId(userService.getMember(), note.getId()).orElseThrow();
        noteEntity.setTime(Instant.now().toEpochMilli());
        noteEntity.setContent(note.getContent());
        noteEntity.setTitle(note.getTitle());

        return noteMapper.to(noteRepository.save(noteEntity));
    }

    @Override
    public Status deleteNote(Long id) {
        NoteEntity noteEntity = noteRepository.findByAuthorAndId(userService.getMember(), id).orElseThrow();
        noteRepository.deleteById(noteEntity.getId());
        return new Status();
    }
}
