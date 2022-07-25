package net.artux.pdanetwork.models.story.seller;

import lombok.Data;
import net.artux.pdanetwork.models.items.ArmorDto;
import net.artux.pdanetwork.models.items.ArtifactDto;
import net.artux.pdanetwork.models.items.ItemDto;
import net.artux.pdanetwork.models.items.WeaponDto;

import java.util.List;

@Data
public class SellerDto {

    private int id;
    private String name;
    private String icon;
    private String image;
    private List<ArmorDto> armors;
    private List<WeaponDto> weapons;
    private List<ArtifactDto> artifacts;
    private List<ItemDto> items;

}
