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
public class FriendRelationEntity extends BaseEntity {

    @OneToOne
    private UserEntity user1;
    @OneToOne
    private UserEntity user2;
    private FriendRelation relation;



}
