package net.artux.pdanetwork.models.feed;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ArticleDto(UUID id,
                         String title,
                         String image,
                         String description,
                         String content,
                         Integer views,
                         Long likes,
                         Long comments,
                         Set<String> tags,
                         Instant published) {

    public String getUrl() {
        return "/feed/" + id;
    }

}
