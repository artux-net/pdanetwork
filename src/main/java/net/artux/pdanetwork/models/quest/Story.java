package net.artux.pdanetwork.models.quest;

import lombok.Data;
import net.artux.pdanetwork.models.user.enums.Role;

import java.util.HashMap;
import java.util.List;

@Data
public class Story {

    private long id;
    private String title;
    private String desc;
    private String icon;
    private Role access = Role.TESTER;
    private HashMap<Long, Chapter> chapters;
    private HashMap<Long, GameMap> maps;
    private List<Mission> missions;

    public int stageCount() {
        return getChapters().values().stream().mapToInt(chapter -> chapter.getStages().size()).sum();
    }

    public int pointCount() {
        return getMaps().values().stream().mapToInt(gameMap -> gameMap.getPoints().size()).sum();
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
