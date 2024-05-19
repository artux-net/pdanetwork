package net.artux.pdanetwork.models.feed;

import java.time.Instant;
import java.util.UUID;

public record ArticleSimpleDto(UUID id,
                               String title,
                               String image,
                               String description,
                               Integer views,
                               Long likes,
                               Long comments,
                               Instant published) {

    public String getUrl() {
        return "/feed/" + id;
    }
}
