package net.artux.pdanetwork.models.quest;

import lombok.Data;

@Data
public class Checkpoint {

    private String parameter;
    private String title;
    private int chapterId;
    private int stageId;

}
