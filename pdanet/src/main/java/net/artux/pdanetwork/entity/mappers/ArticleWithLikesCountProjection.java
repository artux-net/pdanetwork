package net.artux.pdanetwork.entity.mappers;

import java.util.UUID;

public interface ArticleWithLikesCountProjection {

    UUID getId();
    String getTitle();
    Long getLikesCount();

}
