package net.artux.pdanetwork.entity.items;

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
        return WearableEntity.class.isAssignableFrom(getTypeClass());
    }

    public boolean isCountable() {
        return switch (this) {
            case BULLET, MEDICINE, ARTIFACT -> true;
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
