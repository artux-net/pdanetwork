package net.artux.pdanetwork.models.profile;

import lombok.Getter;
import lombok.Setter;
import net.artux.pdanetwork.models.profile.items.Armor;
import net.artux.pdanetwork.models.profile.items.Artifact;
import net.artux.pdanetwork.models.profile.items.Item;
import net.artux.pdanetwork.models.profile.items.Weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@lombok.Data
@Getter
@Setter
public class Data {

    private Equipment equipment = new Equipment();
    private Stats stats = new Stats();

    public List<Armor> armors = new ArrayList<>();
    public List<Weapon> weapons = new ArrayList<>();
    public List<Artifact> artifacts = new ArrayList<>();
    public List<Item> items = new ArrayList<>();

    public List<Story> stories = new ArrayList<>();
    public Parameters parameters = new Parameters();
    private HashMap<String, String> temp = new HashMap<>();

    public Data() {
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>(armors);
        items.addAll(weapons);
        items.addAll(artifacts);
        items.addAll(this.items);
        return items;
    }

    public Optional<Item> getItemByHashCode(int hashCode){
        for (Item item:getAllItems()){
            if (item.hashCode() == hashCode){
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

}
