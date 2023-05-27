package net.artux.pdanetwork.models.quest;

import lombok.Data;

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
