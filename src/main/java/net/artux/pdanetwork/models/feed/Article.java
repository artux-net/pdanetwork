package net.artux.pdanetwork.models.feed;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Article extends FeedModel {

    private String content;

    public Article() {
        setPublished(Instant.now().toEpochMilli());
    }

    public Article(String title, String image, List<String> tags, String description, String content) {
        super(title, image, tags, description, Instant.now().toEpochMilli());
        this.content = content;
    }


}
