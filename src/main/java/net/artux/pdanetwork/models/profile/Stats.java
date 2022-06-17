
package net.artux.pdanetwork.models.profile;

import java.util.ArrayList;
import java.util.List;

public class Stats {

    private List<AchievementDTO> achievements = new ArrayList<>();

    public Stats() {
    }

    public List<AchievementDTO> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<AchievementDTO> achievements) {
        this.achievements = achievements;
    }

}
