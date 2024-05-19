
package net.artux.pdanetwork.entity.items;


import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "user_medicine")
public class MedicineEntity extends ItemEntity {

    private float stamina;
    private float radiation;
    private float health;

}
