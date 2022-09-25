package net.artux.pdanetwork.models.user.dto;

import lombok.Data;
import net.artux.pdanetwork.models.items.*;
import net.artux.pdanetwork.models.story.ParameterDto;
import net.artux.pdanetwork.models.story.StoryStateDto;
import net.artux.pdanetwork.models.user.gang.Gang;

import java.util.List;

@Data
public class StoryData {

    private String name;
    private String nickname;
    private String login;
    private String avatar;
    private int money;
    private int xp;
    private int pdaId;
    private Gang gang;

    private List<ParameterDto> parameters;
    private List<StoryStateDto> storyStates;

    private List<ArmorDto> armors;
    private List<ArtifactDto> artifacts;
    private List<DetectorDto> detectors;
    private List<MedicineDto> medicines;
    private List<WeaponDto> weapons;
    private List<ItemDto> bullets;

}
