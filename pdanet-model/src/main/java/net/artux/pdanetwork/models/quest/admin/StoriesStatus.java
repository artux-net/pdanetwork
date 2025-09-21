package net.artux.pdanetwork.models.quest.admin;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class StoriesStatus {

    private Instant readTime;
    private int userStories;
    private List<StoryInfoAdmin> stories;

}
