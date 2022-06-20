package net.artux.pdanetwork.models.story.seller;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class Seller {

    private int id;
    private String name;
    private String avatar;
    private HashMap<Integer, List<Integer>> items;

}
