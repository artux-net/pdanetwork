package net.artux.pdanetwork.models;

import lombok.Data;
import net.artux.pdanetwork.models.user.dto.StoryData;

@Data
public class Status {

    private boolean success;
    private int code;
    private String description;
    private StoryData storyData;

    public Status() {
        code = 200;
    }

    public Status(boolean success, String description) {
        this.success = success;
        if (success)
            code = 200;
        else
            code = 400;
        this.description = description;
    }

    public Status(boolean success, String description, StoryData storyData) {
        this(success, description);
        this.storyData = storyData;
    }

    public Status(boolean success) {
        this.success = success;
    }

    public boolean isFailure() {
        return !success;
    }

}
