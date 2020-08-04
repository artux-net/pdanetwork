
package net.artux.pdanetwork.models.profile;

public class Story {

    private Integer id;
    private Integer lastChapter;
    private Integer lastStage;

    public Story() {
    }

    public Story(String[] values) {
        this.id = Integer.parseInt(values[0]);
        this.lastChapter = Integer.parseInt(values[1]);
        this.lastStage = Integer.parseInt(values[2]);
    }

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


}
