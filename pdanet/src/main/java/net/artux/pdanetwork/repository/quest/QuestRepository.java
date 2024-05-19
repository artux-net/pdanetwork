package net.artux.pdanetwork.repository.quest;

import net.artux.pdanetwork.entity.quest.StoryBackup;
import net.artux.pdanetwork.entity.quest.StoryType;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestRepository extends JpaRepository<StoryBackup, UUID> {

    Optional<StoryBackup> findByTypeAndStoryIdAndArchiveIsFalse(StoryType type, Long storyId);

    Page<StoryBackup> findAllByAuthor(UserEntity user, Pageable pageable);

    Page<StoryBackup> findAllByTypeAndArchive(StoryType type, boolean archive, Pageable pageable);

    List<StoryBackup> findAllByTypeAndArchiveIsFalse(StoryType type);

    Page<StoryBackup> findAllByTypeAndAuthorAndArchive(StoryType type, UserEntity user, boolean archive, Pageable pageable);

    Page<StoryBackup> findAllByAuthorAndArchive(UserEntity user, boolean archive, Pageable pageable);

    Page<StoryBackup> findAllByAuthorAndArchiveAndStoryId(UserEntity user, boolean archive, Long storyId, Pageable pageable);

    Optional<StoryBackup> findByAuthorAndStoryIdAndArchiveFalse(UserEntity user, Long id);

    Page<StoryBackup> findAllByStoryIdAndTypeAndArchive(Long storyId, StoryType type, boolean archive, Pageable pageable);

    Optional<StoryBackup> findByStoryIdAndTypeAndArchiveIsFalse(Long storyId, StoryType type);
}
