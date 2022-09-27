package net.artux.pdanetwork.models.quest;

import lombok.Data;

@Data
public class Sound {

    private int id;
    private int type;
    private String name;
    private String url;
    private String[] params;

}
