package net.artux.pdanetwork.models.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatisticDto {

    public Integer distance = 0;
    public Integer killedEnemies = 0;
    public Integer killedMutants = 0;
    public Integer secretFound = 0;
    public Integer collectedArtifacts = 0;
    public Integer boughtItems = 0;
    public Integer soldItems = 0;

}
