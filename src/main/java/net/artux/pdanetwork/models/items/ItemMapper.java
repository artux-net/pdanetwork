package net.artux.pdanetwork.models.items;

import net.artux.pdanetwork.entity.items.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "weight", source = "entity.base.weight")
    @Mapping(target = "type", source = "entity.base.type")
    @Mapping(target = "title", source = "entity.base.title")
    @Mapping(target = "price", source = "entity.base.price")
    @Mapping(target = "icon", source = "entity.base.icon")
    @Mapping(target = "baseId", source = "entity.base.id")
    ItemDto item(BulletEntity entity);

    @Mapping(target = "weight", source = "entity.base.weight")
    @Mapping(target = "type", source = "entity.base.type")
    @Mapping(target = "title", source = "entity.base.title")
    @Mapping(target = "price", source = "entity.base.price")
    @Mapping(target = "icon", source = "entity.base.icon")
    @Mapping(target = "baseId", source = "entity.base.id")
    ItemDto item(UsualItemEntity entity);

    List<ItemDto> items(List<BulletEntity> itemEntities);

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



}
