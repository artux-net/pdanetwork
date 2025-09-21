package net.artux.pdanetwork.entity.feed;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.user.UserEntity;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "article_like")
@NoArgsConstructor
public class ArticleLikeEntity {

    @Id
    @EmbeddedId
    private LikeArticleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("articleId")
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    public ArticleLikeEntity(UserEntity user, ArticleEntity article) {
        id = new LikeArticleId(user.getId(), article.getId());
        this.user = user;
        this.article = article;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArticleLikeEntity that = (ArticleLikeEntity) o;

        return !Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
