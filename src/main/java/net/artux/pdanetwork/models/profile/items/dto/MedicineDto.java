package net.artux.pdanetwork.models.profile.items.dto;

import lombok.Data;

@Data
public class MedicineDto extends ItemDto{

    private float stamina;
    private float radiation;
    private float health;

}
