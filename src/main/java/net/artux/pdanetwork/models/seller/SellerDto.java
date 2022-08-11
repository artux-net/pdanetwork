package net.artux.pdanetwork.models.seller;

import lombok.Data;
import net.artux.pdanetwork.models.items.ArmorDto;
import net.artux.pdanetwork.models.items.ArtifactDto;
import net.artux.pdanetwork.models.items.ItemDto;
import net.artux.pdanetwork.models.items.WeaponDto;

import java.util.List;

@Data
public class SellerDto {

    private long id;
    private String name;
    private String icon;
    private String image;
    private List<ArmorDto> armors;
    private List<WeaponDto> weapons;
    private List<ArtifactDto> artifacts;
    private List<ItemDto> bullets;
    private float buyCoefficient;
    private float sellCoefficient;


}
