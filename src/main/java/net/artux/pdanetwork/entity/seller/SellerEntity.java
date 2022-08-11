package net.artux.pdanetwork.entity.seller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.items.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "seller")
public class SellerEntity {

    @Id
    private long id;
    private String name;
    private String icon;
    private String image;
    private float buyCoefficient;
    private float sellCoefficient;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "seller_bullet",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "bullet_id"))
    private Set<BulletEntity> bullets = new HashSet<>();

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
            case BULLET -> bullets.add((BulletEntity) itemEntity);
            case ARMOR -> armors.add((ArmorEntity) itemEntity);
            case PISTOL, RIFLE -> weapons.add((WeaponEntity) itemEntity);
            case ARTIFACT -> artifacts.add((ArtifactEntity) itemEntity);
            case DETECTOR -> detectors.add((DetectorEntity) itemEntity);
            case MEDICINE -> medicines.add((MedicineEntity) itemEntity);
        }
    }

    public void removeItem(ItemEntity itemEntity) {
        switch (itemEntity.getBase().getType()) {
            case BULLET -> bullets.remove((BulletEntity) itemEntity);
            case ARMOR -> armors.remove((ArmorEntity) itemEntity);
            case PISTOL, RIFLE -> weapons.remove((WeaponEntity) itemEntity);
            case ARTIFACT -> artifacts.remove((ArtifactEntity) itemEntity);
            case DETECTOR -> detectors.remove((DetectorEntity) itemEntity);
            case MEDICINE -> medicines.remove((MedicineEntity) itemEntity);
        }
    }

}
