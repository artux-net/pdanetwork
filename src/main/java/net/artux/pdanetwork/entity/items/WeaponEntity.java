package net.artux.pdanetwork.entity.items;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "user_weapon")
public class WeaponEntity extends ConditionalEntity {

    private float precision;
    private float speed;
    private float damage;
    private int bulletQuantity;
    private int bulletBaseId;

    @Embedded
    private WeaponSound sounds;

}
