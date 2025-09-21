package net.artux.pdanetwork.service.items;

import net.artux.pdanetwork.entity.items.ArmorEntity;
import net.artux.pdanetwork.entity.items.ArtifactEntity;
import net.artux.pdanetwork.entity.items.BulletEntity;
import net.artux.pdanetwork.entity.items.DetectorEntity;
import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.MedicineEntity;
import net.artux.pdanetwork.entity.items.UsualItemEntity;
import net.artux.pdanetwork.entity.items.WeaponEntity;
import net.artux.pdanetwork.models.items.ItemDto;
import net.artux.pdanetwork.models.items.WeaponDto;

public enum ItemType {
    ARMOR("Одежда", 4, ArmorEntity.class),
    PISTOL("Пистолеты", 0, WeaponEntity.class),
    RIFLE("Винтовки", 1, WeaponEntity.class),
    MEDICINE("Лекарства", 6, MedicineEntity.class),
    ARTIFACT("Артефакты", 3, ArtifactEntity.class),
    DETECTOR("Детекторы", 5, DetectorEntity.class),
    BULLET("Боеприпасы", 2, BulletEntity.class),
    ITEM("Предметы", 7, UsualItemEntity.class);

    private final String title;
    private final int id;
    private final Class<? extends ItemEntity> typeClass;

    ItemType(String title, int id, Class<? extends ItemEntity> typeClass) {
        this.title = title;
        this.id = id;
        this.typeClass = typeClass;
    }

    public String getTitle() {
        return title;
    }

    public int getTypeId() {
        return id;
    }

    public Class<? extends ItemEntity> getTypeClass() {
        return typeClass;
    }

    public boolean isWearable() {
        return WeaponDto.class.isAssignableFrom(getTypeClass());
    }

    public boolean isCountable() {
        return switch (this) {
            case BULLET, MEDICINE, ITEM -> true;
            default -> false;
        };
    }

    public static ItemType getByTypeId(int typeId) {
        for (ItemType type : ItemType.values()) {
            if (type.getTypeId() == typeId)
                return type;
        }
        return ItemType.BULLET;
    }

    public static ItemType getByClass(Class tClass) {
        for (ItemType type : ItemType.values()) {
            if (type.getTypeClass() == tClass)
                return type;
        }
        throw new RuntimeException("Can not find itemType");
    }
}
