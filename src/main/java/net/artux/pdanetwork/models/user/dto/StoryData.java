package net.artux.pdanetwork.models.user.dto;

import lombok.Data;
import net.artux.pdanetwork.models.profile.items.dto.*;
import net.artux.pdanetwork.models.profile.story.ParameterEntity;
import net.artux.pdanetwork.models.profile.story.StoryStateEntity;
import net.artux.pdanetwork.models.profile.story.dto.ParameterDto;
import net.artux.pdanetwork.models.profile.story.dto.StoryStateDto;

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
