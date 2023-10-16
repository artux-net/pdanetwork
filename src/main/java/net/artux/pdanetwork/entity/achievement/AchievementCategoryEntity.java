package net.artux.pdanetwork.entity.achievement;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "achievement_category")
public class AchievementCategoryEntity extends BaseEntity{

    private String name;
    private String title;
    private String image;
    private String description;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<AchievementEntity> achievements;

}
