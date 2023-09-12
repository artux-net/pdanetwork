package net.artux.pdanetwork.models.feed;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record ArticleDto(UUID id,
                         String title,
                         String image,
                         Set<String> tags,
                         String description,
                         String url,
                         String content,
                         int likes,
                         int comments,
                         int views,
                         Instant published) {

}
