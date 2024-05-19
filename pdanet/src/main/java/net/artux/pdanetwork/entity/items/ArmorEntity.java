
package net.artux.pdanetwork.entity.items;


import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "user_armor")
public class ArmorEntity extends ConditionalEntity {

    private float thermalProtection;
    private float electricProtection;
    private float chemicalProtection;
    private float radioProtection;
    private float psyProtection;
    private float damageProtection;
    private float condition; // 0 - 100

}
