package net.artux.pdanetwork.dto;

import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.entity.feed.TagEntity;

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

    public ArticleDto(ArticleEntity article, Long likes, Long comments) {
        this(article.getId(),
                article.getTitle(),
                article.getImage(),
                article.getDescription(),
                article.getContent(),
                article.getViews(),
                likes,
                comments,
                article.getTags().stream().map(TagEntity::getTitle).collect(Collectors.toSet()),
                article.getPublished());
    }

    public String getUrl() {
        return "/feed/" + id;
    }

}
