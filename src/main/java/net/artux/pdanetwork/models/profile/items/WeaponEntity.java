package net.artux.pdanetwork.models.profile.items;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "weapon")
public class WeaponEntity extends WearableEntity {

    private float precision;
    private float speed;
    private float damage;
    private float condition;
    private int bulletQuantity;
    private int bulletId;

}
