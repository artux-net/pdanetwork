package net.artux.pdanetwork.models.items;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.artux.pdanetwork.entity.items.WeaponSound;

@Data
@EqualsAndHashCode(callSuper = true)
public class WeaponDto extends WearableDto {

    private float precision;
    private float speed;
    private float damage;
    private float condition;
    private int bulletQuantity;
    private int bulletId;
    private  float distance;
    private WeaponSound sounds;

}
