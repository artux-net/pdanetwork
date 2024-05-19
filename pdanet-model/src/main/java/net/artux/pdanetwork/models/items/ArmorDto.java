package net.artux.pdanetwork.models.items;

import lombok.Data;

@Data
public class ArmorDto extends WearableDto {

    private float thermalProtection;
    private float electricProtection;
    private float chemicalProtection;
    private float radioProtection;
    private float psyProtection;
    private float damageProtection;
    private float condition;

}
