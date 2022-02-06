
package net.artux.pdanetwork.models.profile;

import java.util.ArrayList;
import java.util.List;

public class Stats {

    private List<Achievement> achievements = new ArrayList<>();

    public Stats() {
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }

}
