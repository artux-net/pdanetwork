
package com.pdanetwork.models.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @SerializedName("equipment")
    @Expose
    private Equipment equipment = new Equipment();
    @SerializedName("stats")
    @Expose
    private Stats stats = new Stats();
    @SerializedName("items")
    @Expose
    private List<com.pdanetwork.models.profile.Item> items = new ArrayList<>();
    @SerializedName("stories")
    @Expose
    private List<Story> stories = new ArrayList<>();
    @SerializedName("params")
    @Expose
    public Params params = new Params();

    public Equipment getEquipment() {
        return equipment;
    }

    public Stats getStats() {
        return stats;
    }

    public List<com.pdanetwork.models.profile.Item> getItems() {
        return items;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

}
