package net.artux.pdanetwork.models.achievement;

import lombok.NoArgsConstructor;
import net.artux.pdanetwork.models.BaseEntity;
import net.artux.pdanetwork.models.achievement.AchievementEntity;
import net.artux.pdanetwork.models.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@lombok.Data
@NoArgsConstructor
@Entity
@Table(name = "achievement_user")
public class UserAchievementEntity extends BaseEntity {

    @ManyToOne
    private UserEntity user;
    @ManyToOne
    private AchievementEntity achievement;

}
