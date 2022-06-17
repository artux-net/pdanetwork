package net.artux.pdanetwork.models.gang;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.models.BaseEntity;
import net.artux.pdanetwork.models.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@RequiredArgsConstructor
@Setter
@Getter
public class GangRelationDto{

    private int loners;
    private int bandits;
    private int military;
    private int liberty;
    private int duty;
    private int monolith;
    private int mercenaries; // наебники
    private int scientists;
    private int clearSky;

    public GangRelationDto(GangRelationEntity gangRelationEntity) {
        this.loners = gangRelationEntity.getLoners();
        this.bandits = gangRelationEntity.getBandits();
        this.military = gangRelationEntity.getMilitary();
        this.liberty = gangRelationEntity.getLiberty();
        this.duty = gangRelationEntity.getDuty();
        this.monolith = gangRelationEntity.getMonolith();
        this.mercenaries = gangRelationEntity.getMercenaries();
        this.scientists = gangRelationEntity.getScientists();
        this.clearSky = gangRelationEntity.getClearSky();
    }
}
