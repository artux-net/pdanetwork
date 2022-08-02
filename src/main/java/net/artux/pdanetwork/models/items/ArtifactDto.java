package net.artux.pdanetwork.models.items;

import lombok.Data;

@Data
public class ArtifactDto extends WearableDto{

    private int health;
    private int radio;
    private int damage;
    private int bleeding;
    private int thermal;
    private int chemical;
    private int endurance;
    private int electric;

}
