package net.artux.pdanetwork.entity.user.relationship;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class RelationshipKey implements Serializable {

    private static final long serialVersionUID = 7335006184470162569L;
    @Column(name = "user1_id")
    private UUID firstUserId;

    @Column(name = "user2_id")
    private UUID secondUserId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelationshipKey that = (RelationshipKey) o;
        return Objects.equals(firstUserId, that.firstUserId) && Objects.equals(secondUserId, that.secondUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstUserId, secondUserId);
    }
}
