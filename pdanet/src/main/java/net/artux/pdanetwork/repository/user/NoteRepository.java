package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.entity.note.NoteEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public interface NoteRepository extends JpaRepository<NoteEntity, UUID> {

    @NotNull
    @Override
    @Query(value = "select n from NoteEntity n where n.id = ?1")
    Optional<NoteEntity> findById(@NotNull UUID uuid);

    @Query(value = "select n from NoteEntity n where n.author = ?1")
    List<NoteEntity> findAllByAuthor(UserEntity userEntity);

}
