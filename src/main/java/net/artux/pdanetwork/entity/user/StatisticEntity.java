package net.artux.pdanetwork.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "user_statistic")
@NoArgsConstructor
public class StatisticEntity extends BaseEntity {

    @OneToOne
    public UserEntity user;

    @Column(columnDefinition = "integer default 0")
    public Integer distance = 0;
    @Column(columnDefinition = "integer default 0")
    public Integer killedEnemies = 0;
    @Column(columnDefinition = "integer default 0")
    public Integer killedMutants = 0;
    @Column(columnDefinition = "integer default 0")
    public Integer secretFound = 0;
    @Column(columnDefinition = "integer default 0")
    public Integer collectedArtifacts = 0;
    @Column(columnDefinition = "integer default 0")
    public Integer boughtItems = 0;
    @Column(columnDefinition = "integer default 0")
    public Integer soldItems = 0;

}
