package net.artux.pdanetwork.entity.achievement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;
import net.artux.pdanetwork.entity.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_achievement")
public class UserAchievementEntity extends BaseEntity {

    @ManyToOne
    private UserEntity user;
    @ManyToOne
    private AchievementEntity achievement;

}
