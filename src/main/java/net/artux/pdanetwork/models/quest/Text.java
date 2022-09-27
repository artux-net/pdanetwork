package net.artux.pdanetwork.models.quest;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class Text {

    private String text;
    private HashMap<String, List<String>> condition;

}
