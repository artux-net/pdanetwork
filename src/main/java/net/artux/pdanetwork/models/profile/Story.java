
package net.artux.pdanetwork.models.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Story {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("lastChapter")
    @Expose
    private Integer lastChapter;
    @SerializedName("lastStage")
    @Expose
    private Integer lastStage;
    @SerializedName("state")
    @Expose
    private State state = new State();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(Integer lastChapter) {
        this.lastChapter = lastChapter;
    }

    public Integer getLastStage() {
        return lastStage;
    }

    public void setLastStage(Integer lastStage) {
        this.lastStage = lastStage;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
