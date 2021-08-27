package net.artux.pdanetwork.servlets.Feed.Models;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Getter
@Setter
@RequiredArgsConstructor
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
