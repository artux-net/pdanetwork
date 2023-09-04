package net.artux.pdanetwork.models.seller;

import lombok.Data;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Data
public class SellerAdminDto {

    private long id;
    private String name;
    private String icon;
    private String image;
    private float buyCoefficient;
    private float sellCoefficient;

    private List<String> items = new LinkedList<>();

}
