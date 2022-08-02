package net.artux.pdanetwork.entity.seller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.items.*;
import net.artux.pdanetwork.entity.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "seller")
public class SellerEntity extends BaseEntity {

    private String name;
    private String icon;
    private String image;
    private float buyCoefficient;
    private float sellCoefficient;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "seller_item",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private Set<ItemEntity> items = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "seller_armor",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "armor_id"))
    private Set<ArmorEntity> armors = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "seller_weapon",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "weapon_id"))
    private Set<WeaponEntity> weapons = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "seller_medicine",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "medicine_id"))
    private Set<MedicineEntity> medicines = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "seller_artifact",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "artifact_id"))
    private Set<ArtifactEntity> artifacts = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "seller_detector",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "detector_id"))
    private Set<DetectorEntity> detectors = new HashSet<>();

    public void addItem(ItemEntity itemEntity) {
        switch (itemEntity.getBase().getType()) {
            case ITEM:
                items.add(itemEntity);
                break;
            case ARMOR:
                armors.add((ArmorEntity) itemEntity);
                break;
            case PISTOL:
            case RIFLE:
                weapons.add((WeaponEntity) itemEntity);
                break;
            case ARTIFACT:
                artifacts.add((ArtifactEntity) itemEntity);
                break;
            case DETECTOR:
                detectors.add((DetectorEntity) itemEntity);
                break;
            case MEDICINE:
                medicines.add((MedicineEntity) itemEntity);
                break;
        }
    }

    public void removeItem(ItemEntity itemEntity) {
        switch (itemEntity.getBase().getType()) {
            case ITEM:
                items.remove(itemEntity);
                break;
            case ARMOR:
                armors.remove((ArmorEntity) itemEntity);
                break;
            case PISTOL:
            case RIFLE:
                weapons.remove((WeaponEntity) itemEntity);
                break;
            case ARTIFACT:
                artifacts.remove((ArtifactEntity) itemEntity);
                break;
            case DETECTOR:
                detectors.remove((DetectorEntity) itemEntity);
                break;
            case MEDICINE:
                medicines.remove((MedicineEntity) itemEntity);
                break;
        }
    }

}
