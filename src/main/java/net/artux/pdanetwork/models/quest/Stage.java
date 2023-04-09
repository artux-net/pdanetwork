package net.artux.pdanetwork.models.quest;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class Stage {

    private long id;
    private Integer type_stage;
    private String background;
    private int[] music;
    private String title;
    private String message;
    private Integer type_message;
    private List<Text> texts;
    private List<Transfer> transfers;
    private HashMap<String, List<String>> actions;
    private HashMap<String, String> data;

}
