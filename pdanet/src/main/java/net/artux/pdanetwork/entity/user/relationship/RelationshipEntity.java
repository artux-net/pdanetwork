package net.artux.pdanetwork.entity.user.relationship;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.user.UserEntity;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name = "user_relationship")
public class RelationshipEntity {

    @EmbeddedId
    private RelationshipKey relationshipKey;

    @ManyToOne
    @MapsId("firstUserId")
    @JoinColumn(name = "user1_id")
    private UserEntity user1;
    @ManyToOne
    @MapsId("secondUserId")
    @JoinColumn(name = "user2_id")
    private UserEntity user2;

    public RelationshipEntity(UserEntity user1, UserEntity user2) {
        this.user1 = user1;
        this.user2 = user2;
    }
}
