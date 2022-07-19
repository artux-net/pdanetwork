package net.artux.pdanetwork.models.items;

import lombok.Data;

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
