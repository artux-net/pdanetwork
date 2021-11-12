package net.artux.pdanetwork.models.feed;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class FeedModel {

    @Id
    private String id;
    private String title;
    private String image;
    private List<String> tags = new ArrayList<>();
    private String description;
    private long published;

    public FeedModel(String title, String image, List<String> tags, String description, long published) {
        this.title = title;
        this.image = image;
        this.tags = tags;
        this.description = description;
        this.published = published;
    }

}
