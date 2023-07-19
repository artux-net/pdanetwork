package net.artux.pdanetwork.models.quest;

import lombok.Data;
import net.artux.pdanetwork.entity.quest.StoryType;

import java.util.UUID;

@Data
public class StoryBackupEditDto {

    private UUID id;

    private String comment;
    private boolean archive;
    private StoryType type;

}
