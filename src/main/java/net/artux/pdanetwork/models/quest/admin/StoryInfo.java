package net.artux.pdanetwork.models.quest.admin;

import lombok.Data;

@Data
public class StoryInfo {

    private long id;
    private String title;
    private String desc;
    private String icon;

    private int chapters;
    private int stages;
    private int points;

}
