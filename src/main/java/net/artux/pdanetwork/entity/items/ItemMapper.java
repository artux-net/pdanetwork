package net.artux.pdanetwork.entity.items;

import net.artux.pdanetwork.models.items.ArmorDto;
import net.artux.pdanetwork.models.items.ArtifactDto;
import net.artux.pdanetwork.models.items.DetectorDto;
import net.artux.pdanetwork.models.items.ItemDto;
import net.artux.pdanetwork.models.items.MedicineDto;
import net.artux.pdanetwork.models.items.WeaponDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto item(ItemEntity itemEntity);
    List<ItemDto> items(List<ItemEntity> itemEntities);

    @Mapping(target = "equipped", expression = "java(itemEntity.isEquipped)")
    ArtifactDto artifact(ArtifactEntity itemEntity);
    List<ArtifactDto> artifacts(List<ArtifactEntity> itemEntities);

    @Mapping(target = "equipped", expression = "java(itemEntity.isEquipped)")
    ArmorDto armor(ArmorEntity itemEntity);
    List<ArmorDto> armors(List<ArmorEntity> itemEntities);
    @Mapping(target = "equipped", expression = "java(itemEntity.isEquipped)")
    WeaponDto weapon(WeaponEntity itemEntity);
    List<WeaponDto> weapons(List<WeaponEntity> itemEntities);

    @Mapping(target = "equipped", expression = "java(itemEntity.isEquipped)")
    DetectorDto detector(DetectorEntity itemEntity);
    List<DetectorDto> detectors(List<DetectorEntity> itemEntities);

    MedicineDto medicine(MedicineEntity itemEntity);
    List<MedicineDto> medicines(List<MedicineEntity> itemEntities);

}
