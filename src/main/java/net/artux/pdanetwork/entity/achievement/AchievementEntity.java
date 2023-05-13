package net.artux.pdanetwork.entity.achievement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "achievement")
public class AchievementEntity extends BaseEntity {

    private String title;
    private String name;
    private String image;
    private String description;

    @Enumerated(EnumType.STRING)
    private AchievementGroup type;

}
