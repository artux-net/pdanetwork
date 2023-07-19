package net.artux.pdanetwork.repository.quest;

import net.artux.pdanetwork.entity.quest.StoryBackup;
import net.artux.pdanetwork.entity.quest.StoryType;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestRepository extends JpaRepository<StoryBackup, UUID> {

    Page<StoryBackup> findAllByAuthor(UserEntity user, Pageable pageable);

    Page<StoryBackup> findAllByType(StoryType type, Pageable pageable);

    List<StoryBackup> findAllByTypeAndArchiveIsFalse(StoryType type);

    Page<StoryBackup> findAllByTypeAndAuthor(StoryType type, UserEntity user, Pageable pageable);

}
