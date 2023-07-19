package net.artux.pdanetwork.models.quest;

import lombok.Data;
import net.artux.pdanetwork.entity.quest.StoryType;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;

import java.time.Instant;
import java.util.UUID;

@Data
public class StoryBackupDto extends StoryInfo {

    private UUID storageId;

    private String comment;
    private boolean archive;
    private StoryType type;
    private Instant timestamp;
    private SimpleUserDto author;
    private int hashcode;

}
