package net.artux.pdanetwork.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_statistic")
@NoArgsConstructor
public class StatisticEntity {

    @Id
    @Column(name = "user_id")
    private UUID id;

    @MapsId
    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity user;

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

    StatisticEntity(UserEntity user){
        this.user = user;
        id = user.getId();
    }

}
