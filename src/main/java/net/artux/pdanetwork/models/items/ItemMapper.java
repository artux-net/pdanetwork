package net.artux.pdanetwork.models.items;

import net.artux.pdanetwork.entity.items.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Named(value = "item")
    ItemDto item(ItemEntity entity);

    @IterableMapping(qualifiedByName = "item")
    List<ItemDto> items(List<ItemEntity> itemEntities);

    @Mapping(target = "weight", source = "entity.base.weight")
    @Mapping(target = "type", source = "entity.base.type")
    @Mapping(target = "title", source = "entity.base.title")
    @Mapping(target = "price", source = "entity.base.price")
    @Mapping(target = "icon", source = "entity.base.icon")
    @Mapping(target = "baseId", source = "entity.base.id")
    @Mapping(target = "equipped", expression = "java(entity.isEquipped())")
    ArtifactDto artifact(ArtifactEntity entity);

    List<ArtifactDto> artifacts(List<ArtifactEntity> itemEntities);

    @Mapping(target = "weight", source = "entity.base.weight")
    @Mapping(target = "type", source = "entity.base.type")
    @Mapping(target = "title", source = "entity.base.title")
    @Mapping(target = "price", source = "entity.base.price")
    @Mapping(target = "icon", source = "entity.base.icon")
    @Mapping(target = "baseId", source = "entity.base.id")
    @Mapping(target = "equipped", expression = "java(entity.isEquipped())")
    ArmorDto armor(ArmorEntity entity);

    List<ArmorDto> armors(List<ArmorEntity> itemEntities);

    @Mapping(target = "bulletId", source = "bulletBaseId")
    @Mapping(target = "weight", source = "entity.base.weight")
    @Mapping(target = "type", source = "entity.base.type")
    @Mapping(target = "title", source = "entity.base.title")
    @Mapping(target = "price", source = "entity.base.price")
    @Mapping(target = "icon", source = "entity.base.icon")
    @Mapping(target = "baseId", source = "entity.base.id")
    @Mapping(target = "equipped", expression = "java(entity.isEquipped())")
    WeaponDto weapon(WeaponEntity entity);

    List<WeaponDto> weapons(List<WeaponEntity> itemEntities);

    @Mapping(target = "weight", source = "entity.base.weight")
    @Mapping(target = "type", source = "entity.base.type")
    @Mapping(target = "title", source = "entity.base.title")
    @Mapping(target = "price", source = "entity.base.price")
    @Mapping(target = "icon", source = "entity.base.icon")
    @Mapping(target = "baseId", source = "entity.base.id")
    @Mapping(target = "equipped", expression = "java(entity.isEquipped())")
    DetectorDto detector(DetectorEntity entity);

    List<DetectorDto> detectors(List<DetectorEntity> itemEntities);

    @Mapping(target = "weight", source = "entity.base.weight")
    @Mapping(target = "type", source = "entity.base.type")
    @Mapping(target = "title", source = "entity.base.title")
    @Mapping(target = "price", source = "entity.base.price")
    @Mapping(target = "icon", source = "entity.base.icon")
    @Mapping(target = "baseId", source = "entity.base.id")
    MedicineDto medicine(MedicineEntity entity);

    List<MedicineDto> medicines(List<MedicineEntity> itemEntities);

    default ItemDto any(ItemEntity entity1) {
        return switch (entity1.getBase().getType()) {
            case ARMOR -> armor((ArmorEntity) entity1);
            case PISTOL, RIFLE -> weapon((WeaponEntity) entity1);
            case MEDICINE -> medicine((MedicineEntity) entity1);
            case ARTIFACT -> artifact((ArtifactEntity) entity1);
            case DETECTOR -> detector((DetectorEntity) entity1);
            case ITEM -> item(entity1);
        };
    }

}
