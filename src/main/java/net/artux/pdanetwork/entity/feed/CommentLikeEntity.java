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
@Table(name = "comment_like")
@NoArgsConstructor
public class CommentLikeEntity {

    @EmbeddedId
    private LikeCommentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("commentId")
    @JoinColumn(name = "comment_id")
    private CommentEntity comment;

    public CommentLikeEntity(UserEntity user, CommentEntity comment) {
        id = new LikeCommentId(user.getId(), comment.getId());
        this.user = user;
        this.comment = comment;
    }
}
