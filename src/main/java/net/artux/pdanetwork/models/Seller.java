package net.artux.pdanetwork.models;

import net.artux.pdanetwork.models.profile.items.Armor;
import net.artux.pdanetwork.models.profile.items.Artifact;
import net.artux.pdanetwork.models.profile.items.Item;
import net.artux.pdanetwork.models.profile.items.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Seller {

    public int id;
    public String name;
    public String avatar;

    private final List<Armor> armors = new ArrayList<>();
    private final List<Weapon> pistols = new ArrayList<>();
    private final List<Weapon> rifles = new ArrayList<>();
    private final List<Artifact> artifacts = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();

    public List<Item> getAllItems() {
        for (Weapon weapon : pistols)
            weapon.type = 0;
        for (Weapon weapon : rifles)
            weapon.type = 1;
        for (Artifact artifact : artifacts)
            artifact.type = 3;
        for (Armor armor : armors)
            armor.type = 4;
        for (Item item : items)
            item.type = 2;
        List<Item> items = new ArrayList<>(armors);
        items.addAll(pistols);
        items.addAll(rifles);
        items.addAll(artifacts);
        items.addAll(this.items);
        return items;
    }
}
