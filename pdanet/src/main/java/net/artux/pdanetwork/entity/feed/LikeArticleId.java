package net.artux.pdanetwork.entity.feed;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class LikeArticleId implements Serializable {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "article_id")
    private UUID articleId;

    public LikeArticleId(UUID userId, UUID articleId) {
        this.userId = userId;
        this.articleId = articleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LikeArticleId articleId1 = (LikeArticleId) o;

        if (!Objects.equals(userId, articleId1.userId)) return false;
        return Objects.equals(articleId, articleId1.articleId);
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (articleId != null ? articleId.hashCode() : 0);
        return result;
    }
}
