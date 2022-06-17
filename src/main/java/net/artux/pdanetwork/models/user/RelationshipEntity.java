package net.artux.pdanetwork.models.user;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@lombok.Data
@RequiredArgsConstructor
@Entity
@Table(name = "relationship")
public class RelationshipEntity extends BaseEntity {

    @OneToOne
    private UserEntity user1;
    @OneToOne
    private UserEntity user2;

    public RelationshipEntity(UserEntity user1, UserEntity user2) {
        this.user1 = user1;
        this.user2 = user2;
    }
}
