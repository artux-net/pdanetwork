package net.artux.pdanetwork.models.quest;

import lombok.Data;

import java.util.List;

@Data
public class GameMap {

    private long id;
    private String title;
    private String texture;
    private String tilesTexture;
    private String boundsTexture;
    private String blurTexture;
    private String defPos;
    private List<Point> points;
    private List<Spawn> spawns;

}
