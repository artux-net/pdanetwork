package net.artux.pdanetwork.entity.user;

import lombok.Getter;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "user_story_state")
public class StoryStateEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;
    private int storyId;
    private int chapterId;
    private int stageId;
    private boolean over;
    private boolean current;

}
