package net.artux.pdanetwork.models.user.gang;

public enum Gang {
    LONERS("Одиночки", 0),
    BANDITS("Бандиты", 1),
    MILITARY("Военные", 2),
    LIBERTY("Свобода", 3),
    DUTY("Долг", 4),
    MONOLITH("Монолит", 5),
    MERCENARIES("Наемники", 6),
    SCIENTISTS("Ученые", 7),
    CLEAR_SKY("Чистое небо", 8);

    private final String name;
    private final int id;

    Gang(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static Gang getById(int id) {
        for (Gang gang : Gang.values())
            if (gang.id == id)
                return gang;
        return null;
    }
}
