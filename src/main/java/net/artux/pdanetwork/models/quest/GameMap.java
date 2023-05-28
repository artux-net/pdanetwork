package net.artux.pdanetwork.models.quest;

import lombok.Data;
import net.artux.pdanetwork.models.quest.map.Point;
import net.artux.pdanetwork.models.quest.map.Spawn;

import java.util.List;

@Data
public class GameMap {

    private long id;
    private String title;
    private String tmx;
    private String defPos;
    private List<Point> points;
    private List<Spawn> spawns;

}
