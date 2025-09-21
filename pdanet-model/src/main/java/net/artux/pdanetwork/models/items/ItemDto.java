package net.artux.pdanetwork.models.items;

import lombok.Data;

import java.util.UUID;

@Data
public class ItemDto {

    protected UUID id;
    protected ItemType type;
    protected String icon;
    protected String title;
    protected int baseId;
    protected float weight;
    protected int price;
    protected int quantity;

}
