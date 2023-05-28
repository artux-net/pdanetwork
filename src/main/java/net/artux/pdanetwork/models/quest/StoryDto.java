package net.artux.pdanetwork.models.quest;

import lombok.Data;
import net.artux.pdanetwork.models.user.enums.Role;

import java.util.Map;

@Data
public class StoryDto {

    private Long id;
    private String title;
    private String desc;
    private String icon;
    private int[] needs;
    private Role access = Role.TESTER;
    private Map<Long, ChapterDto> chapters;
    private Map<Long, GameMap> maps;

    public int stageCount() {
        return getChapters().values().stream().mapToInt(chapter -> chapter.getStages().size()).sum();
    }

    public int chapterCount() {
        return (int) getChapters().values().stream().count();
    }

    public int pointCount() {
        return getMaps().values().stream().mapToInt(gameMap -> gameMap.getPoints().size()).sum();
    }

}
