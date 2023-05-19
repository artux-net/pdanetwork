package net.artux.pdanetwork.models.user.gang;

import net.artux.pdanetwork.models.enums.EnumGetter;

public enum Gang implements EnumGetter {
    LONERS("Одиночки", 0),
    BANDITS("Бандиты", 1),
    MILITARY("Военные", 2),
    LIBERTY("Свобода", 3),
    DUTY("Долг", 4),
    MONOLITH("Монолит", 5),
    MERCENARIES("Наемники", 6),
    SCIENTISTS("Ученые", 7),
    CLEAR_SKY("Чистое небо", 8);

    private final String title;
    private final int id;

    Gang(String title, int id) {
        this.title = title;
        this.id = id;
    }

    @Override
    public String getId() {
        return this.name();
    }

    public String getTitle() {
        return title;
    }

    public static Gang getById(int id) {
        for (Gang gang : Gang.values())
            if (gang.id == id)
                return gang;
        return null;
    }
}
