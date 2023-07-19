package net.artux.pdanetwork.service.quest;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.quest.StoryBackup;
import net.artux.pdanetwork.entity.quest.StoryType;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.repository.quest.QuestRepository;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.util.SimpleS3Service;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestAdminServiceImpl implements QuestAdminService {

    private final UserService userService;
    private final QuestRepository questRepository;
    private final SimpleS3Service<StoryBackup> s3Service;

    @Override
    public ResponsePage<StoryBackup> getBackups(QueryPage queryPage) {
        return null;
    }

    @Override
    public ResponsePage<StoryBackup> getBackupsByType(StoryType storyType) {
        return null;
    }
}
