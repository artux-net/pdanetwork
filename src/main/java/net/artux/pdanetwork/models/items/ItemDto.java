package net.artux.pdanetwork.models.items;

import lombok.Data;
import net.artux.pdanetwork.entity.items.ItemType;

@Data
public class ItemDto {

    protected long id;
    protected ItemType type;
    protected String icon;
    protected String title;
    protected int baseId;
    protected float weight;
    protected int price;
    protected int quantity;

}
