package net.artux.pdanetwork.models.user.dto;

import lombok.Data;
import net.artux.pdanetwork.models.items.ArmorDto;
import net.artux.pdanetwork.models.items.ArtifactDto;
import net.artux.pdanetwork.models.items.DetectorDto;
import net.artux.pdanetwork.models.items.ItemDto;
import net.artux.pdanetwork.models.items.MedicineDto;
import net.artux.pdanetwork.models.items.WeaponDto;
import net.artux.pdanetwork.models.story.ParameterDto;
import net.artux.pdanetwork.models.story.StoryStateDto;

import java.util.List;

@Data
public class StoryData {

    private List<ParameterDto> parameters;
    private List<StoryStateDto> storyStates;

    private List<ArmorDto> armors;
    private List<ArtifactDto> artifacts;
    private List<DetectorDto> detectors;
    private List<MedicineDto> medicines;
    private List<WeaponDto> weapons;
    private List<ItemDto> items;

}
