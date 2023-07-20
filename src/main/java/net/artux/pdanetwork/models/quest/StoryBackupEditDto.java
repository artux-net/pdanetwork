package net.artux.pdanetwork.models.quest;

import lombok.Data;
import net.artux.pdanetwork.entity.quest.StoryType;

@Data
public class StoryBackupEditDto {

    private String comment;
    private boolean archive;
    private StoryType type;

}
