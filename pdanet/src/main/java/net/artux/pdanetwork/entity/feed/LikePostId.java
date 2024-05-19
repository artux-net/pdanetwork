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
public class LikePostId implements Serializable {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "post_id")
    private UUID postId;

    public LikePostId(UUID userId, UUID postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
