package net.artux.pdanetwork.service;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.profile.Data;
import net.artux.pdanetwork.models.profile.items.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemsManager {

    public Data  addWeapon(Data data, WeaponEntity weaponEntity, int quantity) {
        weaponEntity.setQuantity(quantity);
        for (int i = 0; i < quantity; i++) {
            data.weaponEntities.add(weaponEntity);
        }
        return data;
    }

    public Data addArmor(Data data, ArmorEntity armorEntity, int quantity) {
        armorEntity.setQuantity(quantity);
        for (int i = 0; i < quantity; i++) {
            data.armorEntities.add(armorEntity);
        }
        return data;
    }

    public Data addItem(Data data, ItemEntity itemEntity) {
        for (int i = 0; i < data.itemEntities.size(); i++) {
            ItemEntity itemEntity1 = data.itemEntities.get(i);
            if (itemEntity.equals(itemEntity1)) {
                itemEntity1.setQuantity(itemEntity1.getQuantity() + itemEntity.getQuantity());
                data.itemEntities.set(i, itemEntity1);
                return data;
            }
        }
        data.itemEntities.add(itemEntity);
        return data;
    }

    public Data addArtifact(Data data, ArtifactEntity item) {
        for (int i = 0; i < data.artifactEntities.size(); i++) {
            ArtifactEntity artifactEntity1 = data.artifactEntities.get(i);
            if (item.equals(artifactEntity1)) {
                artifactEntity1.setQuantity(artifactEntity1.getQuantity() + item.getQuantity());
                data.artifactEntities.set(i, artifactEntity1);
                return data;
            }
        }
        data.artifactEntities.add(item);
        return data;
    }
}
