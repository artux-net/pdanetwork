package net.artux.pdanetwork.models.user.gang;

public enum Gang {
    LONERS,
    BANDITS,
    MILITARY,
    LIBERTY,
    DUTY,
    MONOLITH,
    MERCENARIES,
    SCIENTISTS,
    CLEAR_SKY;

    public static Gang getById(int id) {
        if (id == 0)
            return Gang.LONERS;
        if (id == 1)
            return Gang.BANDITS;
        if (id == 2)
            return Gang.MILITARY;
        if (id == 3)
            return Gang.LIBERTY;
        if (id == 4)
            return Gang.DUTY;
        if (id == 5)
            return Gang.MONOLITH;
        if (id == 6)
            return Gang.MERCENARIES;
        if (id == 7)
            return Gang.SCIENTISTS;
        return Gang.CLEAR_SKY;
    }
}
