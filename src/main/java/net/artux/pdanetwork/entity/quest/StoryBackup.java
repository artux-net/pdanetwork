package net.artux.pdanetwork.entity.quest;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.user.enums.Role;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table
public class StoryBackup extends BaseEntity {

    private Long storyId;
    private String title;
    private String desc;
    private String icon;
    private String comment;
    private int hashcode;
    private boolean archive;

    private int[] needs;
    private Role access = Role.TESTER;

    private StoryType type;
    private Instant timestamp;
    @ManyToOne
    private UserEntity author;

    public StoryBackup() {
        archive = false;
        timestamp = Instant.now();
    }

}
