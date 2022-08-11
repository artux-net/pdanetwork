package net.artux.pdanetwork.entity.user.gang;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.BaseEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.user.gang.Gang;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@RequiredArgsConstructor
@Entity
@Getter
@Table(name = "user_gang_relation")
public class GangRelationEntity extends BaseEntity {

    @OneToOne
    private UserEntity user;

    private int loners;
    private int bandits;
    private int military;
    private int liberty;
    private int duty;
    private int monolith;
    private int mercenaries; // наебники
    private int scientists;
    private int clearSky;

    public GangRelationEntity(UserEntity user) {
        this.user = user;
    }

    public void addRelation(Gang gang, int value) {
        switch (gang) {
            case LONERS:
                loners += value;
                break;
            case BANDITS:
                bandits += value;
            case MILITARY:
                military += value;
                break;
            case LIBERTY:
                liberty += value;
                break;
            case DUTY:
                duty += value;
                break;
            case MERCENARIES:
                mercenaries += value;
                break;
            case MONOLITH:
                monolith += value;
                break;
            case SCIENTISTS:
                scientists += value;
                break;
            case CLEAR_SKY:
                clearSky += value;
                break;
        }
    }

    public void setRelation(Gang gang, int value) {
        switch (gang) {
            case LONERS:
                loners = value;
                break;
            case BANDITS:
                bandits = value;
            case MILITARY:
                military = value;
                break;
            case LIBERTY:
                liberty = value;
                break;
            case DUTY:
                duty = value;
                break;
            case MERCENARIES:
                mercenaries = value;
                break;
            case MONOLITH:
                monolith = value;
                break;
            case SCIENTISTS:
                scientists = value;
                break;
            case CLEAR_SKY:
                clearSky = value;
                break;
        }
    }



}
