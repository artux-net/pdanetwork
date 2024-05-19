package net.artux.pdanetwork.models.items;

public enum ItemType {
    ARMOR("Одежда", 4, ArmorDto.class),
    PISTOL("Пистолеты", 0, WeaponDto.class),
    RIFLE("Винтовки", 1, WeaponDto.class),
    MEDICINE("Лекарства", 6, MedicineDto.class),
    ARTIFACT("Артефакты", 3, ArtifactDto.class),
    DETECTOR("Детекторы", 5, DetectorDto.class),
    BULLET("Боеприпасы", 2, ItemDto.class),
    ITEM("Предметы", 7, ItemDto.class);

    private final String title;
    private final int id;
    private final Class<? extends ItemDto> typeClass;

    ItemType(String title, int id, Class<? extends ItemDto> typeClass) {
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

    public Class<? extends ItemDto> getTypeClass() {
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
