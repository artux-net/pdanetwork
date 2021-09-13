package net.artux.pdanetwork.service;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.profile.Data;
import net.artux.pdanetwork.models.profile.items.*;
import net.artux.pdanetwork.service.files.Types;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemsManager {

    public Data  addWeapon(Data data, Weapon weapon, int quantity) {
        weapon.setQuantity(quantity);
        for (int i = 0; i < quantity; i++) {
            data.weapons.add(weapon);
        }
        return data;
    }

    public Data addArmor(Data data, Armor armor, int quantity) {
        armor.setQuantity(quantity);
        for (int i = 0; i < quantity; i++) {
            data.armors.add(armor);
        }
        return data;
    }

    public Data addItem(Data data, Item item) {
        for (int i = 0; i < data.items.size(); i++) {
            Item item1 = data.items.get(i);
            if (item.equals(item1)) {
                item1.setQuantity(item1.getQuantity() + item.getQuantity());
                data.items.set(i, item1);
                return data;
            }
        }
        data.items.add(item);
        return data;
    }

    public Data addArtifact(Data data, Artifact item) {
        for (int i = 0; i < data.artifacts.size(); i++) {
            Artifact artifact1 = data.artifacts.get(i);
            if (item.equals(artifact1)) {
                artifact1.setQuantity(artifact1.getQuantity() + item.getQuantity());
                data.artifacts.set(i, artifact1);
                return data;
            }
        }
        data.artifacts.add(item);
        return data;
    }
}
