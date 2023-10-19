package net.artux.pdanetwork.models.feed;

import java.util.UUID;

public interface ArticleWithLikesCountProjection {

    UUID getId();
    String getTitle();
    Long getLikesCount();

}
