package net.artux.pdanetwork.models.items;

import lombok.Data;
import net.artux.pdanetwork.entity.items.DetectorType;

@Data
public class DetectorDto extends WearableDto{

    private DetectorType detectorType;

}
