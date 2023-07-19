package net.artux.pdanetwork.service.quest;

import net.artux.pdanetwork.entity.quest.StoryBackup;
import net.artux.pdanetwork.entity.quest.StoryType;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;

public interface QuestAdminService {

    ResponsePage<StoryBackup> getBackups(QueryPage queryPage);

    ResponsePage<StoryBackup> getBackupsByType(StoryType storyType);

}
