package net.artux.pdanetwork.models.quest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.artux.pdanetwork.models.user.enums.Role;

import java.util.Collection;
import java.util.Locale;

@Data
public class Story {

    @NotNull
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String desc;
    @NotBlank
    private String icon;
    private int[] needs;
    private Role access = Role.TESTER;
    private Collection<Chapter> chapters;
    private Collection<GameMap> maps;

    private Locale locale;

    public int stageCount() {
        return getChapters().stream().mapToInt(chapter -> chapter.getStages().size()).sum();
    }

    public int missionsCount() {
        return getChapters().stream().mapToInt(chapter -> chapter.getMissions().size()).sum();
    }

    public int chapterCount() {
        return getChapters().size();
    }

    public int pointCount() {
        return getMaps().stream().mapToInt(gameMap -> gameMap.getPoints().size()).sum();
    }

}
