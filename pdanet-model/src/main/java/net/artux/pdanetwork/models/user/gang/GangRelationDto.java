package net.artux.pdanetwork.models.user.gang;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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

}
