package net.artux.pdanetwork.service.util;

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
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.repository.quest.QuestRepository;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.utills.security.IsCreator;
import net.artux.pdanetwork.utills.security.IsModerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class QuestBackupService extends SimpleS3Service<Story> {

    private final QuestRepository questRepository;
    private final UserService userService;
    private final PageService pageService;
    private final BackupMapper backupMapper;

    protected QuestBackupService(@Value("${s3.bucket.name}") String bucketName,
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

    @IsCreator
    public StoryBackupDto saveBackup(Story story, StoryType type, String comment) {
        StoryBackup backup = new StoryBackup();
        backup.setType(type);
        backup.setTitle(story.getTitle());
        backup.setIcon(story.getIcon());
        backup.setDesc(story.getDesc());
        backup.setAuthor(userService.getUserById());
        backup.setComment(comment);
        backup.setNeeds(story.getNeeds());
        backup.setAccess(story.getAccess());
        backup.setTimestamp(Instant.now());

        backup = questRepository.save(backup);
        put(backup.getId().toString(), story);

        return backupMapper.dto(backup);
    }

    @IsCreator
    public boolean deleteBackup(UUID id) {
        StoryBackup backup = questRepository.findById(id).orElseThrow();
        UserEntity currentUser = userService.getUserById();
        boolean usersStory = backup.getAuthor().getId() == currentUser.getId();
        if (usersStory) {
            questRepository.delete(backup);
            return true;
        } else if (currentUser.getRole().getPriority() > Role.CREATOR.getPriority()) {
            questRepository.delete(backup);
            return true;
        }
        questRepository.deleteById(id);
        return true;
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

    @IsCreator
    public Story getBackup(UUID id) {
        return get(id.toString());
    }

    @IsModerator
    public ResponsePage<StoryBackupDto> getBackups(StoryType type, QueryPage queryPage) {
        Pageable pageable = pageService.getPageable(queryPage);
        return ResponsePage.of(questRepository.findAllByType(type, pageable).map(backupMapper::dto));
    }

    @IsCreator
    public ResponsePage<StoryBackupDto> getUserBackups(StoryType type, QueryPage queryPage) {
        UserEntity user = userService.getUserById();
        Pageable pageable = pageService.getPageable(queryPage);
        return ResponsePage.of(questRepository.findAllByTypeAndAuthor(type, user, pageable).map(backupMapper::dto));
    }

}
