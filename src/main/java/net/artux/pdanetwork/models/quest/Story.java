package net.artux.pdanetwork.models.quest;

import lombok.Data;
import net.artux.pdanetwork.models.user.enums.Role;

import java.util.Collection;

@Data
public class Story {

    private Long id;
    private String title;
    private String desc;
    private String icon;
    private int[] needs;
    private Role access = Role.TESTER;
    private Collection<Chapter> chapters;
    private Collection<GameMap> maps;

    public int stageCount() {
        return getChapters().stream().mapToInt(chapter -> chapter.getStages().size()).sum();
    }

    public int chapterCount() {
        return getChapters().size();
    }


    public int pointCount() {
        return getMaps().stream().mapToInt(gameMap -> gameMap.getPoints().size()).sum();
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
