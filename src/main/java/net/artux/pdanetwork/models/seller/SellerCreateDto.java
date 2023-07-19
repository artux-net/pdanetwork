package net.artux.pdanetwork.models.seller;

import lombok.Data;

@Data
public class SellerCreateDto {

    private long id;
    private String name;
    private String icon;
    private String image;
    private float buyCoefficient;
    private float sellCoefficient;

}
