package net.artux.pdanetwork.models.seller;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Seller {

    private long id;
    private String name;
    private String icon;
    private String image;
    private float buyCoefficient;
    private float sellCoefficient;
    private Set<String> items = new HashSet<>();

}
