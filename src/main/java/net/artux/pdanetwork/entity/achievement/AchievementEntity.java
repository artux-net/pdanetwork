package net.artux.pdanetwork.entity.achievement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "user_achievement")
public class AchievementEntity extends BaseEntity {

    private String title;
    private String name;
    private String image;
    private String description;

    @Enumerated(EnumType.STRING)
    private AchievementGroup type;

}
