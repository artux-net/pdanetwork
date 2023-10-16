package net.artux.pdanetwork.entity.achievement;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;
import net.artux.pdanetwork.entity.user.UserEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "achievement")
public class AchievementEntity extends BaseEntity{

    private String name;
    private String title;
    private String image;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private AchievementCategoryEntity category;

    @ElementCollection
    @CollectionTable(name = "achievement_condition",
            joinColumns = {@JoinColumn(name = "achievement_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    private Map<String, HashSet<String>> condition;

    @Enumerated(EnumType.STRING)
    private AchievementGroup type;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<UserEntity> users;

}
