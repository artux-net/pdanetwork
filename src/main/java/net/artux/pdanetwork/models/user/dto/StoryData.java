package net.artux.pdanetwork.models.user.dto;

import lombok.Data;
import net.artux.pdanetwork.models.items.*;
import net.artux.pdanetwork.models.story.ParameterDto;
import net.artux.pdanetwork.models.story.StoryStateDto;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.models.user.gang.Gang;
import net.artux.pdanetwork.models.user.gang.GangRelationDto;

import java.time.Instant;
import java.util.List;

@Data
public class StoryData {

    private String name;
    private String nickname;
    private String login;
    private String avatar;
    private Role role;
    private int pdaId;

    private int money;
    private int xp;
    private Gang gang;
    private GangRelationDto relations;
    private Instant registration;
    private Instant lastLoginAt;

    private List<ParameterDto> parameters;
    private List<StoryStateDto> storyStates;

    private List<ArmorDto> armors;
    private List<ArtifactDto> artifacts;
    private List<DetectorDto> detectors;
    private List<MedicineDto> medicines;
    private List<WeaponDto> weapons;
    private List<ItemDto> bullets;
    private List<ItemDto> items;

    private boolean chatBan;
    private boolean receiveEmails;
}
