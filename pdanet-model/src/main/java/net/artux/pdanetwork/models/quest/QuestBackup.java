package net.artux.pdanetwork.models.quest;

import lombok.Data;

import java.util.List;

@Data
public class QuestBackup {

    private List<Story> publicStories;
    private List<StoryBackupDto> backups;

}
