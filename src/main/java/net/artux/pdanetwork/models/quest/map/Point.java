package net.artux.pdanetwork.models.quest.map;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
public class Point {

    private UUID id;
    public int type;
    private String name;
    private String pos;
    private HashMap<String, String> data;
    public HashMap<String, List<String>> condition;

}
