
package net.artux.pdanetwork.models.profile;

import net.artux.pdanetwork.models.profile.items.Armor;
import net.artux.pdanetwork.models.profile.items.Artifact;
import net.artux.pdanetwork.models.profile.items.Item;
import net.artux.pdanetwork.models.profile.items.Weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Data {

    private Equipment equipment;
    private Stats stats = new Stats();

    private List<Armor> armors = new ArrayList<>();
    private List<Weapon> weapons = new ArrayList<>();
    private List<Artifact> artifacts = new ArrayList<>();
    private List<Item> items = new ArrayList<>();

    private List<Story> stories = new ArrayList<>();
    public Parameters parameters = new Parameters();
    private HashMap<String, String> temp = new HashMap<>();

    public Data() {
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public Stats getStats() {
        return stats;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Story> getStories() {
        return new ArrayList<>();
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public List<Armor> getArmors() {
        return armors;
    }

    public void setArmors(List<Armor> armors) {
        this.armors = armors;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public HashMap<String, String> getTemp() {
        return temp;
    }

    public void setTemp(HashMap<String, String> temp) {
        this.temp = temp;
    }
}
