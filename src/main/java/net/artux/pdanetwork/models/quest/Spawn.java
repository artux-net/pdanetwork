package net.artux.pdanetwork.models.quest;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class Spawn {

    private int id;
    private int r;
    private int n;
    private String pos;
    private boolean angry;
    private boolean ignorePlayer;
    private HashMap<String, String> data;
    private HashMap<String, List<String>> condition;

}
