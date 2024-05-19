package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.entity.note.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public interface NoteRepository extends JpaRepository<NoteEntity, UUID> {

    @Override
    @Query(value = "select n from NoteEntity n where n.author.id = ?#{principal.id} and n.id = ?1")
    Optional<NoteEntity> findById(UUID uuid);

    @Query(value = "select n from NoteEntity n where n.author.id = ?#{principal.id}")
    List<NoteEntity> findAllByAuthor();

}
