package net.artux.pdanetwork.models.story;

import lombok.Data;

@Data
public class StoryStateDto {

    private int storyId;
    private int chapterId;
    private int stageId;
    private boolean over;
    private boolean current;

}
