package net.artux.pdanetwork.models;

import lombok.NoArgsConstructor;
import net.artux.pdanetwork.models.user.FriendRelation;
import net.artux.pdanetwork.models.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@lombok.Data
@NoArgsConstructor
@Entity
@Table(name = "achievement")
public class AchievementEntity extends BaseEntity {

    private String title;
    private String image;
    private String desc;

}
