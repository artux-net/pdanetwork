package net.artux.pdanetwork.entity.feed;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.user.UserEntity;

@Getter
@Setter
@Entity
@Table(name = "article_like")
@NoArgsConstructor
public class ArticleLikeEntity {

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
}
