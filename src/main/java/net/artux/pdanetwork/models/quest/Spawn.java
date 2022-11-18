package net.artux.pdanetwork.models.quest;

import lombok.Data;
import net.artux.pdanetwork.models.user.gang.Gang;

import java.util.HashMap;
import java.util.List;

@Data
public class Spawn {

    private int id;
    private String title;
    private String description;
    private Gang group;
    private Strength strength;
    private int r;
    private int n;
    private String pos;
    private HashMap<String, List<String>> data;
    private HashMap<String, List<String>> condition;
    private HashMap<String, List<String>> actions;

}
