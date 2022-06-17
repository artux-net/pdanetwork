package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.models.note.NoteEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface NoteRepository extends JpaRepository<NoteEntity, Long> {

    Optional<NoteEntity> findByAuthorAndId(UserEntity userEntity, Long id);
    List<NoteEntity> findAllByAuthor(UserEntity userEntity);

}
