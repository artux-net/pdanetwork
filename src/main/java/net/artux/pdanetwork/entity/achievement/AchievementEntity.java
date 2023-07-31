package net.artux.pdanetwork.entity.achievement;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;
import net.artux.pdanetwork.entity.user.UserEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
    @ManyToOne
    private AchievementCategoryEntity category;

    @ElementCollection
    @CollectionTable(name = "achievement_condition",
            joinColumns = {@JoinColumn(name = "achievement_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    private Map<String, HashSet<String>> condition;

    @Enumerated(EnumType.STRING)
    private AchievementGroup type;

    @ManyToMany(mappedBy = "achievements", fetch = FetchType.LAZY)
    private List<UserEntity> users;

}
