package net.artux.pdanetwork.entity.feed;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.artux.pdanetwork.entity.user.UserEntity;

import java.io.Serializable;
import java.util.UUID;


@Getter
@Setter
@Embeddable
@EqualsAndHashCode
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
}
