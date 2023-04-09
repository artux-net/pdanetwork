package net.artux.pdanetwork.models.quest;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class Transfer {

    private long stage;
    private HashMap<String, List<String>> condition;
    private String text;

}
