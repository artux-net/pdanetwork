package net.artux.pdanetwork.models.quest;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class Story {

    private long id;
    private String title;
    private String desc;
    private String icon;
    private HashMap<Long, Chapter> chapters;
    private HashMap<Long, Map> maps;

    public int stageCount() {
        return getChapterList().stream().mapToInt(chapter -> chapter.getStages().size()).sum();
    }

    public int pointCount() {
        return getMapList().stream().mapToInt(map -> map.getPoints().size()).sum();
    }

    public List<Chapter> getChapterList() {
        return chapters.values().stream().toList();
    }

    public List<Map> getMapList() {
        return maps.values().stream().toList();
    }
}
