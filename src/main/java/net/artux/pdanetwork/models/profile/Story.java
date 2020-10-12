
package net.artux.pdanetwork.models.profile;

public class Story {

    private Integer storyId;
    private Integer lastChapter;
    private Integer lastStage;

    public Story() {
    }

    public Story(String[] values) {
        this.storyId = Integer.parseInt(values[0]);
        this.lastChapter = Integer.parseInt(values[1]);
        this.lastStage = Integer.parseInt(values[2]);
    }

    public Story(Integer storyId, Integer lastChapter, Integer lastStage) {
        this.storyId = storyId;
        this.lastChapter = lastChapter;
        this.lastStage = lastStage;
    }

    public Integer getStoryId() {
        return storyId;
    }

    public void setStoryId(Integer storyId) {
        this.storyId = storyId;
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

    @Override
    public String toString() {
        return "Story{" +
                "storyId=" + storyId +
                ", lastChapter=" + lastChapter +
                ", lastStage=" + lastStage +
                '}';
    }
}
