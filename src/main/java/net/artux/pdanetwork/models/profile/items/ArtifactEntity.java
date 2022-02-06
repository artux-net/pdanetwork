package net.artux.pdanetwork.models.profile.items;

import lombok.Data;

import java.util.Objects;

@Data
public class ArtifactEntity extends ItemEntity {

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
        ArtifactEntity artifactEntity = (ArtifactEntity) o;
        return anomal_id == artifactEntity.anomal_id &&
                health == artifactEntity.health &&
                radio == artifactEntity.radio &&
                damage == artifactEntity.damage &&
                bleeding == artifactEntity.bleeding &&
                thermal == artifactEntity.thermal &&
                chemical == artifactEntity.chemical &&
                endurance == artifactEntity.endurance &&
                electric == artifactEntity.electric;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), anomal_id, health, radio, damage, bleeding, thermal, chemical, endurance, electric);
    }
}
