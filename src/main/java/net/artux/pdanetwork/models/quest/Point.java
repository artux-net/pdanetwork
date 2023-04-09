package net.artux.pdanetwork.models.quest;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class  Point {

    public int type;
    private String name;
    private String pos;
    private HashMap<String, String> data;
    public HashMap<String, List<String>> condition;

}
