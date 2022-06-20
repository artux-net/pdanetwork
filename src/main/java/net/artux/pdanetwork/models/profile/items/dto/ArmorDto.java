package net.artux.pdanetwork.models.profile.items.dto;

import lombok.Data;
import net.artux.pdanetwork.models.profile.items.ItemType;

@Data
public class ArmorDto extends WearableDto{

    private float thermal_pr;
    private float electric_pr;
    private float chemical_pr;
    private float radio_pr;
    private float psy_pr;
    private float damage_pr;
    private float condition;

}
