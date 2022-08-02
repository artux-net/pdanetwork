package net.artux.pdanetwork.models.user.gang;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.user.gang.GangRelationEntity;

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
