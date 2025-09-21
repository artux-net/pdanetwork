package net.artux.pdanetwork.entity.seller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.items.ArmorEntity;
import net.artux.pdanetwork.entity.items.ArtifactEntity;
import net.artux.pdanetwork.entity.items.BulletEntity;
import net.artux.pdanetwork.entity.items.DetectorEntity;
import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.MedicineEntity;
import net.artux.pdanetwork.entity.items.WeaponEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "seller_bullet",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "bullet_id"))
    private List<BulletEntity> bullets = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "seller_armor",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "armor_id"))
    private List<ArmorEntity> armors = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "seller_weapon",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "weapon_id"))
    private List<WeaponEntity> weapons = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "seller_medicine",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "medicine_id"))
    private List<MedicineEntity> medicines = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "seller_artifact",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "artifact_id"))
    private List<ArtifactEntity> artifacts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "seller_detector",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "detector_id"))
    private List<DetectorEntity> detectors = new ArrayList<>();

    @Nullable
    public ItemEntity findItem(long baseId) {
        for (ItemEntity item : getAllItems()) {
            if (item.getBase().getId() == baseId)
                return item;
        }
        return null;
    }

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

    @JsonIgnore
    public List<ItemEntity> getAllItems() {
        List<ItemEntity> list = new LinkedList<>();
        list.addAll(armors);
        list.addAll(weapons);
        list.addAll(bullets);
        list.addAll(medicines);
        list.addAll(artifacts);
        list.addAll(detectors);
        return list;
    }

}
