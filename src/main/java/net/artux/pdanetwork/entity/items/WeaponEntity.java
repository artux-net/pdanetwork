package net.artux.pdanetwork.entity.items;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_weapon")
public class WeaponEntity extends ConditionalEntity {

    private float precision;
    private float speed;
    private float damage;
	private float distance;
    private int bulletQuantity;
    private int bulletBaseId;

}
