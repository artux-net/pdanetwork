package net.artux.pdanetwork.models.feed;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ArticleFullDto(UUID id,
                             String title,
                             String image,
                             List<String> tags,
                             String description,
                             String url,
                             String content,
                             Instant published) {

}
