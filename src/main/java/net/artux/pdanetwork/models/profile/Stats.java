
package net.artux.pdanetwork.models.profile;

import java.util.ArrayList;
import java.util.List;

public class Stats {

    private List<Achievement> achievements = new ArrayList<>();
    private Battles battles = new Battles();

    public Stats() {
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    public Battles getBattles() {
        return battles;
    }

    public void setBattles(Battles battles) {
        this.battles = battles;
    }

}
