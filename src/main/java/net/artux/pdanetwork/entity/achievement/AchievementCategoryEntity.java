package net.artux.pdanetwork.entity.achievement;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "achievement_category")
public class AchievementCategoryEntity {

    @Id
    private String name;
    private String title;
    private String image;
    private String description;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<AchievementEntity> achievements;

}
