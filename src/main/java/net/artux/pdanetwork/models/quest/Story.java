package net.artux.pdanetwork.models.quest;

import lombok.Data;
import net.artux.pdanetwork.models.user.enums.Role;

import java.util.HashMap;

@Data
public class Story {

    private Long id;
    private String title;
    private String desc;
    private String icon;
    private Role access = Role.TESTER;
    private HashMap<Long, Chapter> chapters;
    private HashMap<Long, GameMap> maps;

    public int stageCount() {
        return getChapters().values().stream().mapToInt(chapter -> chapter.getStages().size()).sum();
    }

    public int chapterCount() {
        return (int) getChapters().values().stream().count();
    }


    public int pointCount() {
        return getMaps().values().stream().mapToInt(gameMap -> gameMap.getPoints().size()).sum();
    }

    /*public Mission getMissionByParam(String param) {
        if (missions != null)
            for (Mission mission : missions) {
                if (mission.getCheckpointWithParam(param) != null)
                    return mission;
            }
        return null;
    }*/
}
