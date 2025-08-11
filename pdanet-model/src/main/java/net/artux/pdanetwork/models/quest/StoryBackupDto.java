package net.artux.pdanetwork.models.quest;

import lombok.Data;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;
import net.artux.pdanetwork.models.user.enums.Role;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Data
public class StoryBackupDto {

    private UUID storageId;

    private Long storyId;
    private String title;
    private String icon;
    private int[] needs;
    private Role access = Role.TESTER;
    private Locale locale = Locale.forLanguageTag("RU");

    private String message;
    private boolean archive;
    private StoryType type;
    private Instant timestamp;
    private SimpleUserDto author;
    private int hashcode;

}
