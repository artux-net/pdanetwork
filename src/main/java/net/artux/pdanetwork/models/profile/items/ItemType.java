package net.artux.pdanetwork.models.profile.items;

public enum ItemType {
    ARMOR {
        @Override
        public int getTypeId() {
            return 4;
        }

        @Override
        public Class<? extends ItemEntity> getTypeClass() {
            return ArmorEntity.class;
        }
    },
    PISTOL {
        @Override
        public int getTypeId() {
            return 0;
        }

        @Override
        public Class<? extends ItemEntity> getTypeClass() {
            return WeaponEntity.class;
        }
    },
    RIFLE {
        @Override
        public int getTypeId() {
            return 1;
        }

        @Override
        public Class<? extends ItemEntity> getTypeClass() {
            return WeaponEntity.class;
        }
    },
    MEDICINE {
        @Override
        public int getTypeId() {
            return 6;
        }

        @Override
        public Class<? extends ItemEntity> getTypeClass() {
            return MedicineEntity.class;
        }
    },
    ARTIFACT {
        @Override
        public int getTypeId() {
            return 3;
        }

        @Override
        public Class<? extends ItemEntity> getTypeClass() {
            return ArtifactEntity.class;
        }
    },
    DETECTOR {
        @Override
        public int getTypeId() {
            return 5;
        }

        @Override
        public Class<? extends ItemEntity> getTypeClass() {
            return DetectorEntity.class;
        }
    },
    ITEM {
        @Override
        public int getTypeId() {
            return 2;
        }

        @Override
        public Class<? extends ItemEntity> getTypeClass() {
            return ItemEntity.class;
        }
    };

    public abstract int getTypeId();

    public Class<? extends ItemEntity> getTypeClass() {
        return ItemEntity.class;
    }

    public static ItemType getByTypeId(int typeId) {
        for (ItemType type : ItemType.values()) {
            if (type.getTypeId() == typeId)
                return type;
        }
        return ItemType.ITEM;
    }
}
