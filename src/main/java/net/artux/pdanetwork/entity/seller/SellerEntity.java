package net.artux.pdanetwork.entity.seller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.items.*;
import net.artux.pdanetwork.models.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
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
    private String description;
    private float buyCoefficient;
    private float sellCoefficient;

    @ManyToMany
    private Set<ItemEntity> items = new HashSet<>();
    @ManyToMany
    private Set<ArmorEntity> armors = new HashSet<>();
    @ManyToMany
    private Set<WeaponEntity> weapons = new HashSet<>();
    @ManyToMany
    private Set<MedicineEntity> medicines = new HashSet<>();
    @ManyToMany
    private Set<ArtifactEntity> artifacts = new HashSet<>();
    @ManyToMany
    private Set<DetectorEntity> detectors = new HashSet<>();

}
