
package net.artux.pdanetwork.models.profile;

import net.artux.pdanetwork.models.profile.items.ArmorEntity;
import net.artux.pdanetwork.models.profile.items.ArtifactEntity;
import net.artux.pdanetwork.models.profile.items.ItemEntity;
import net.artux.pdanetwork.models.profile.items.WeaponEntity;

import java.util.ArrayList;
import java.util.List;


public class Equipment {

    private ArmorEntity armorEntity;
    private WeaponEntity firstWeaponEntity;
    private WeaponEntity secondWeaponEntity;
    private ItemEntity detector;
    private List<ArtifactEntity> artifactEntities = new ArrayList<>();

    public Equipment() {
    }

    public ArmorEntity getArmor() {
        return armorEntity;
    }

    public void setArmor(ArmorEntity armorEntity) {
        this.armorEntity = armorEntity;
    }

    public WeaponEntity getFirstWeapon() {
        return firstWeaponEntity;
    }

    public void setFirstWeapon(WeaponEntity firstWeaponEntity) {
        this.firstWeaponEntity = firstWeaponEntity;
    }

    public WeaponEntity getSecondWeapon() {
        return secondWeaponEntity;
    }

    public void setSecondWeapon(WeaponEntity secondWeaponEntity) {
        this.secondWeaponEntity = secondWeaponEntity;
    }

    public ItemEntity getDetector() {
        return detector;
    }

    public void setDetector(ItemEntity detector) {
        this.detector = detector;
    }

    public List<ArtifactEntity> getArtifacts() {
        return artifactEntities;
    }

    public void setArtifacts(List<ArtifactEntity> artifactEntities) {
        this.artifactEntities = artifactEntities;
    }
}
