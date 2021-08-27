package net.artux.pdanetwork.models;

import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@Data
@Getter
public class Seller {

    private int id;
    private String name;
    private String avatar;
    private HashMap<Integer, List<Integer>> items;

}
