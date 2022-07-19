
package net.artux.pdanetwork.entity.items;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "medicine")
public class MedicineEntity extends ItemEntity {

    private float stamina;
    private float radiation;
    private float health;

}
