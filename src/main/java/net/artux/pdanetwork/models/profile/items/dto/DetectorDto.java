package net.artux.pdanetwork.models.profile.items.dto;

import lombok.Data;
import net.artux.pdanetwork.models.profile.items.DetectorType;

@Data
public class DetectorDto extends WearableDto{

    private DetectorType detectorType;

}
