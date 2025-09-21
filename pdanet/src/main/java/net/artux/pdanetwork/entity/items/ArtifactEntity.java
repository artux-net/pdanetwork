package net.artux.pdanetwork.entity.items;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "user_artifact")
public class ArtifactEntity extends WearableEntity {

    private int health;
    private int radio;
    private int damage;
    private int bleeding;
    private int thermal;
    private int chemical;
    private int endurance;
    private int electric;

}
