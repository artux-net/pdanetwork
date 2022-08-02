package net.artux.pdanetwork.entity.user;

import lombok.Getter;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "story_state")
public class StoryStateEntity extends BaseEntity {

    @ManyToOne
    private UserEntity player;
    private int storyId;
    private int chapterId;
    private int stageId;
    private boolean over;
    private boolean current;

}
