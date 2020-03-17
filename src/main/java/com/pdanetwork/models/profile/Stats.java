
package com.pdanetwork.models.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stats {

    @SerializedName("achievements")
    @Expose
    private Achievements achievements = new Achievements();
    @SerializedName("battles")
    @Expose
    private Battles battles = new Battles();

    public Achievements getAchievements() {
        return achievements;
    }

    public void setAchievements(Achievements achievements) {
        this.achievements = achievements;
    }

    public Battles getBattles() {
        return battles;
    }

    public void setBattles(Battles battles) {
        this.battles = battles;
    }

}
