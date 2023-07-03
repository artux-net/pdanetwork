package net.artux.pdanetwork.models.items;

import lombok.Data;
import net.artux.pdanetwork.entity.items.ItemType;

import java.util.LinkedList;
import java.util.List;

@Data
public class ItemsContainer {

    private List<ArmorDto> armors;
    private List<WeaponDto> weapons;
    private List<ArtifactDto> artifacts;
    private List<ItemDto> bullets;
    private List<ItemDto> usual;
    private List<MedicineDto> medicines;
    private List<DetectorDto> detectors;

    {
        weapons = new LinkedList<>();
    }

    @SuppressWarnings("unchecked")
    public void set(List<? extends ItemDto> anyList, ItemType type) {
        switch (type){
            case ARMOR -> armors = (List<ArmorDto>) anyList;
            case RIFLE, PISTOL -> weapons.addAll((List<WeaponDto>) anyList);
            case MEDICINE ->  medicines = (List<MedicineDto>) anyList;
            case ITEM -> usual = (List<ItemDto>) anyList;
            case BULLET -> bullets = (List<ItemDto>) anyList;
            case DETECTOR -> detectors = (List<DetectorDto>) anyList;
            case ARTIFACT -> artifacts = (List<ArtifactDto>) anyList;
        }
    }
}
