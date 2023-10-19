package net.artux.pdanetwork.models.feed;

import net.artux.pdanetwork.entity.feed.ArticleEntity;

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

    public ArticleSimpleDto(ArticleEntity article, Long likes, Long comments) {
        this(article.getId(),
                article.getTitle(),
                article.getImage(),
                article.getDescription(),
                article.getViews(),
                likes,
                comments,
                article.getPublished());
    }

    public String getUrl() {
        return "/feed/" + id;
    }
}
