package net.artux.pdanetwork.models.quest.map;

import net.artux.pdanetwork.models.enums.MapEnumGetter;

public enum MapEnum implements MapEnumGetter {
    CH4(0, "ch4.tmx", "map_ch_4.png", "Ч-4"),
    KORDON(1, "kordon.tmx", "map_escape.png", "Кордон"),
    GARBAGE(2, "garbage.tmx", "map_garbage.png", "Свалка"),
    BAR(3, "bar.tmx", "map_bar.png", "Бар", new Position(1800, 1400)),
    DIKIY_BAR(4, "bar.tmx", "map_bar.png", "Дикий бар", new Position(1150, 860)),
    DARK_VALLEY(5, "darkvalley.tmx", "map_darkvalley.png", "Тёмная долина"),
    YANTAR(6, "yantar.tmx", "map_yantar.png", "Янтарь"),
    MILITARY(7, "military.tmx", "map_military.png", "Военные склады"),
    AGROPROM(8, "agroprom.tmx", "map_agroprom.png", "Агропром"),
    MARSH(9, "marsh.tmx", "map_marsh.png", "Болота"),
    EAST_KORDON(10, "kordon.tmx", "map_east_kordon.png", "Восточный кордон");

    private final int id;
    private final String tmx;
    private final String background;
    private final String title;
    private Position defaultPosition;

    MapEnum(int id, String tmx, String background, String title) {
        this.id = id;
        this.tmx = tmx;
        this.background = background;
        this.title = title;
        defaultPosition = new Position(500, 500);
    }

    MapEnum(int id, String tmx, String background, String title, Position defaultPosition) {
        this(id, tmx, background, title);
        this.defaultPosition = defaultPosition;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTmx() {
        return tmx;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Position getDefaultPosition() {
        return defaultPosition;
    }

    @Override
    public String getBackground() {
        return background;
    }
}
