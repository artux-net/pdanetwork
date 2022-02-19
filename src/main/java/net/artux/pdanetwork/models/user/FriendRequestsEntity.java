package net.artux.pdanetwork.models.user;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

@lombok.Data
@RequiredArgsConstructor
@Entity
@Table(name = "friends")
public class FriendRequestsEntity extends BaseEntity {

    @OneToOne
    private UserEntity user1;
    @OneToOne
    private UserEntity user2;

    public FriendRequestsEntity(UserEntity user1, UserEntity user2) {
        this.user1 = user1;
        this.user2 = user2;
    }
}
