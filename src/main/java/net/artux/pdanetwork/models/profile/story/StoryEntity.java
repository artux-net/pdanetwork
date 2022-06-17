package net.artux.pdanetwork.models.profile.story;

import lombok.Getter;
import lombok.Setter;
import net.artux.pdanetwork.models.BaseEntity;
import net.artux.pdanetwork.models.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "story")
public class StoryEntity extends BaseEntity {

    @ManyToOne
    private UserEntity player;
    private int storyId;
    private int chapterId;
    private int stageId;
    private boolean over;
    private boolean current;

}
