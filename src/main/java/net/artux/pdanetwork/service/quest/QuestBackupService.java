package net.artux.pdanetwork.service.quest;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.artux.pdanetwork.entity.quest.StoryBackup;
import net.artux.pdanetwork.entity.quest.StoryType;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.models.quest.BackupMapper;
import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.models.quest.StoryBackupDto;
import net.artux.pdanetwork.models.quest.StoryBackupEditDto;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.repository.quest.QuestRepository;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.util.PageService;
import net.artux.pdanetwork.service.util.SimpleS3Service;
import net.artux.pdanetwork.utills.security.CreatorAccess;
import net.artux.pdanetwork.utills.security.ModeratorAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class QuestBackupService extends SimpleS3Service<Story> {

    private final QuestRepository questRepository;
    private final UserService userService;
    private final PageService pageService;
    private final BackupMapper backupMapper;
    private final Logger logger = LoggerFactory.getLogger(QuestBackupService.class);

    protected QuestBackupService(@Value("${quest.storage.s3.bucket.name}") String bucketName,
                                 AmazonS3 client, ObjectMapper mapper,
                                 QuestRepository questRepository,
                                 UserService userService,
                                 PageService pageService, BackupMapper backupMapper) {
        super(bucketName, client, mapper, Story.class);

        this.questRepository = questRepository;
        this.userService = userService;
        this.pageService = pageService;
        this.backupMapper = backupMapper;
    }

    public List<Story> getPublicStories() {
        LinkedList<Story> response = new LinkedList<>();
        for (StoryBackup backup : questRepository.findAllByTypeAndArchiveIsFalse(StoryType.PUBLIC)) {
            Story story = get(backup.getId().toString());
            response.add(story);
        }
        return response;
    }

    public List<Story> getCommunityStories() {
        LinkedList<Story> response = new LinkedList<>();
        for (StoryBackup backup : questRepository.findAllByTypeAndArchiveIsFalse(StoryType.COMMUNITY)) {
            Story story = get(backup.getId().toString());
            response.add(story);
        }
        return response;
    }

    @ModeratorAccess
    public ResponsePage<StoryBackupDto> getAllBackups(StoryType type, boolean archive, QueryPage queryPage) {
        Pageable pageable = pageService.getPageable(queryPage);
        return ResponsePage.of(questRepository.findAllByTypeAndArchive(type, archive, pageable).map(backupMapper::dto));
    }

    @CreatorAccess
    public StoryBackupDto saveStory(Story story, StoryType type, String comment) {
        UserEntity user = userService.getUserById();
        if (type == StoryType.PUBLIC && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Wrong role");
        }
        int hashcode = story.hashCode();

        logger.info("Загрузка " + type + " истории (id = " + story.getId() + ") пользователем " + user.getLogin() + ", хэш: " + hashcode);

        Optional<StoryBackup> lastBackup = questRepository.findByAuthorAndTypeAndStoryIdAndArchiveFalse(user, type, story.getId());
        if (lastBackup.isPresent()) {
            StoryBackup backup = lastBackup.get();
            backup.setArchive(true);

            logger.info("Предыдущая история признана архивной, хэш: " + hashcode);
            questRepository.save(backup);
        }

        StoryBackup backup = new StoryBackup();
        backup.setType(type);
        backup.setTitle(story.getTitle());
        backup.setIcon(story.getIcon());
        backup.setAuthor(userService.getUserById());
        backup.setMessage(comment);
        backup.setNeeds(story.getNeeds());
        backup.setAccess(story.getAccess());
        backup.setTimestamp(Instant.now());
        backup.setHashcode(story.hashCode());
        backup.setArchive(false);

        backup = questRepository.save(backup);
        put(backup.getId().toString(), story);

        return backupMapper.dto(backup);
    }

    @CreatorAccess
    public ResponsePage<StoryBackupDto> getUserBackups(StoryType type, QueryPage queryPage, boolean archive) {
        UserEntity user = userService.getUserById();
        Pageable pageable = pageService.getPageable(queryPage);
        return ResponsePage.of(questRepository.findAllByTypeAndAuthorAndArchive(type, user, archive, pageable)
                .map(backupMapper::dto));
    }

    @CreatorAccess
    public Story getBackup(UUID id) {
        if (isUserCanManipulateStory(id)) {
            return get(id.toString());
        } else return null;
    }

    @CreatorAccess
    public boolean deleteBackup(UUID id) {
        if (isUserCanManipulateStory(id)) {
            questRepository.deleteById(id);
            delete(id.toString());
            return true;
        }
        return false;
    }

    private boolean isUserCanManipulateStory(UUID id) {
        StoryBackup backup = questRepository.findById(id).orElseThrow();
        UserEntity currentUser = userService.getUserById();
        boolean usersStory = backup.getAuthor().getId() == currentUser.getId();
        if (usersStory)
            return true;
        else
            return currentUser.getRole().getPriority() > Role.CREATOR.getPriority();
    }

    public StoryBackupDto update(UUID id, StoryBackupEditDto dto) {
        StoryBackup backup = questRepository.findById(id).orElseThrow();
        if (isUserCanManipulateStory(id)) {
            backup.setMessage(dto.getComment());
            backup.setType(dto.getType());
            backup.setArchive(dto.isArchive());
            backup.setTimestamp(Instant.now());

            return backupMapper.dto(questRepository.save(backup));
        }
        return null;
    }
}
