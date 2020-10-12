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

    private List<Armor> armors = new ArrayList<>();
    private List<Weapon> weapons = new ArrayList<>();
    private List<Artifact> artifacts = new ArrayList<>();
    private List<Item> items = new ArrayList<>();

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        items.addAll(armors);
        items.addAll(weapons);
        items.addAll(artifacts);
        items.addAll(this.items);
        return items;
    }
}
