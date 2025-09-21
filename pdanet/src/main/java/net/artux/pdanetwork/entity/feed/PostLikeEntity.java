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
@Table(name = "post_like")
@NoArgsConstructor
public class PostLikeEntity {

    @EmbeddedId
    private LikePostId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private PostEntity post;

    public PostLikeEntity(UserEntity user, PostEntity post) {
        id = new LikePostId(user.getId(), post.getId());
        this.user = user;
        this.post = post;
    }
}
