package net.artux.pdanetwork.models.profile.story.dto;

import lombok.Data;

@Data
public class StoryStateDto {

    private int storyId;
    private int chapterId;
    private int stageId;
    private boolean over;
    private boolean current;

}
