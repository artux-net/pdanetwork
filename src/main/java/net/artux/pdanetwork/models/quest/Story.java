package net.artux.pdanetwork.models.quest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class Story {

    private long id;
    private String title;
    private String desc;
    private String icon;
    @JsonIgnore
    private HashMap<Long, Chapter> chapters;
    @JsonIgnore
    private HashMap<Long, GameMap> maps;
    private List<Mission> missions;

    public int stageCount() {
        return getChapters().stream().mapToInt(chapter -> chapter.getStages().size()).sum();
    }

    public int pointCount() {
        return getMaps().stream().mapToInt(gameMap -> gameMap.getPoints().size()).sum();
    }

    public List<Chapter> getChapters() {
        return chapters.values().stream().toList();
    }

    public List<GameMap> getMaps() {
        return maps.values().stream().toList();
    }

    public Mission getMissionByParam(String param) {
        if (missions != null)
            for (Mission mission : missions) {
                if (mission.getCheckpointWithParam(param) != null)
                    return mission;
            }
        return null;
    }
}
