package net.artux.pdanetwork.models.user;

import lombok.NoArgsConstructor;
import net.artux.pdanetwork.models.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@lombok.Data
@NoArgsConstructor
//@RequiredArgsConstructor
@Entity
@Table(name = "friends")
public class FriendsEntity extends BaseEntity {

    @OneToOne
    private UserEntity user1;
    @OneToOne
    private UserEntity user2;

    public FriendsEntity(UserEntity user1, UserEntity user2) {
        this.user1 = user1;
        this.user2 = user2;
    }
}
