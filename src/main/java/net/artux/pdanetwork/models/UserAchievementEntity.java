package net.artux.pdanetwork.models;

import lombok.NoArgsConstructor;
import net.artux.pdanetwork.models.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@lombok.Data
@NoArgsConstructor
//@RequiredArgsConstructor
@Entity
@Table(name = "achievement")
public class UserAchievementEntity extends BaseEntity {

    @OneToOne
    private UserEntity user;
    @OneToOne
    private AchievementEntity achievement;

}
