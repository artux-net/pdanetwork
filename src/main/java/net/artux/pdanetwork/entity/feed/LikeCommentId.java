package net.artux.pdanetwork.entity.feed;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;


@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
public class LikeCommentId implements Serializable {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "comment_id")
    private UUID commentId;

    public LikeCommentId(UUID userId, UUID commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }
}
