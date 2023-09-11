package net.artux.pdanetwork.entity;

import net.artux.pdanetwork.entity.items.ArmorEntity;
import net.artux.pdanetwork.entity.items.ArtifactEntity;
import net.artux.pdanetwork.entity.items.BulletEntity;
import net.artux.pdanetwork.entity.items.DetectorEntity;
import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.MedicineEntity;
import net.artux.pdanetwork.entity.items.UsualItemEntity;
import net.artux.pdanetwork.entity.items.WeaponEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.control.DeepClone;

@Mapper(mappingControl = DeepClone.class, componentModel = "spring")
public interface CloningMapper {

    default ItemEntity itemEntity(ItemEntity entity) {
        return switch (entity.getBasedType()) {
            case ARMOR -> armorEntity((ArmorEntity) entity);
            case PISTOL, RIFLE -> weaponEntity((WeaponEntity) entity);
            case MEDICINE -> medicineEntity((MedicineEntity) entity);
            case ARTIFACT -> artifactEntity((ArtifactEntity) entity);
            case DETECTOR -> detectorEntity((DetectorEntity) entity);
            case ITEM -> usualItemEntity((UsualItemEntity) entity);
            case BULLET -> bulletEntity((BulletEntity) entity);
        };
    }

    @Mapping(target = "owner", ignore = true)
    WeaponEntity weaponEntity(WeaponEntity entity);

    @Mapping(target = "owner", ignore = true)
    ArmorEntity armorEntity(ArmorEntity entity);

    @Mapping(target = "owner", ignore = true)
    ArtifactEntity artifactEntity(ArtifactEntity entity);

    @Mapping(target = "owner", ignore = true)
    DetectorEntity detectorEntity(DetectorEntity entity);

    @Mapping(target = "owner", ignore = true)
    UsualItemEntity usualItemEntity(UsualItemEntity entity);

    @Mapping(target = "owner", ignore = true)
    MedicineEntity medicineEntity(MedicineEntity entity);

    @Mapping(target = "owner", ignore = true)
    BulletEntity bulletEntity(BulletEntity entity);

}
