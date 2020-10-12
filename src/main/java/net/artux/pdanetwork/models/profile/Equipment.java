
package net.artux.pdanetwork.models.profile;

import net.artux.pdanetwork.models.profile.items.Armor;
import net.artux.pdanetwork.models.profile.items.Artifact;
import net.artux.pdanetwork.models.profile.items.Item;
import net.artux.pdanetwork.models.profile.items.Weapon;

import java.util.ArrayList;
import java.util.List;


public class Equipment {

    private Armor armor;
    private Weapon firstWeapon;
    private Weapon secondWeapon;
    private Item detector;
    private List<Artifact> artifacts = new ArrayList<>();

    public Equipment() {
    }

    public Armor getArmor() {
        return armor;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
    }

    public Weapon getFirstWeapon() {
        return firstWeapon;
    }

    public void setFirstWeapon(Weapon firstWeapon) {
        this.firstWeapon = firstWeapon;
    }

    public Weapon getSecondWeapon() {
        return secondWeapon;
    }

    public void setSecondWeapon(Weapon secondWeapon) {
        this.secondWeapon = secondWeapon;
    }

    public Item getDetector() {
        return detector;
    }

    public void setDetector(Item detector) {
        this.detector = detector;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }
}
