
package net.artux.pdanetwork.models.profile;

public class Story {

    public Integer storyId;
    public Integer lastChapter;
    public Integer lastStage;

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

    @Override
    public String toString() {
        return "Story{" +
                "storyId=" + storyId +
                ", lastChapter=" + lastChapter +
                ", lastStage=" + lastStage +
                '}';
    }
}
