package net.artux.pdanetwork.models.profile.items;

import lombok.Data;

import java.util.Objects;

@Data
public class Artifact extends Item {

    private int anomal_id;

    private int health;
    private int radio;
    private int damage;
    private int bleeding;
    private int thermal;
    private int chemical;
    private int endurance;
    private int electric;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Artifact artifact = (Artifact) o;
        return anomal_id == artifact.anomal_id &&
                health == artifact.health &&
                radio == artifact.radio &&
                damage == artifact.damage &&
                bleeding == artifact.bleeding &&
                thermal == artifact.thermal &&
                chemical == artifact.chemical &&
                endurance == artifact.endurance &&
                electric == artifact.electric;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), anomal_id, health, radio, damage, bleeding, thermal, chemical, endurance, electric);
    }
}
