package net.artux.pdanetwork.models.quest;

import lombok.Data;
import net.artux.pdanetwork.models.quest.map.MapEnum;
import net.artux.pdanetwork.models.quest.map.Point;
import net.artux.pdanetwork.models.quest.map.Spawn;

import java.util.LinkedList;
import java.util.List;

@Data
public class GameMap {

    private long id;
    private String title;
    private String tmx;
    private String defPos;
    private List<Point> points;
    private List<Spawn> spawns;

    public GameMap() {
    }

    public GameMap(MapEnum mapEnum) {
        this.id = mapEnum.getId();
        this.title = mapEnum.getTitle();
        this.tmx = mapEnum.getTmx();
        this.defPos = mapEnum.getDefaultPosition().x() + ":" + mapEnum.getDefaultPosition().y();
        points = new LinkedList<>();
        spawns = new LinkedList<>();
    }
}
