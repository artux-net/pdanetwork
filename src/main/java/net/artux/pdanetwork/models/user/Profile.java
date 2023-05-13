package net.artux.pdanetwork.models.user;

import lombok.Data;
import net.artux.pdanetwork.models.user.enums.FriendRelation;
import net.artux.pdanetwork.models.user.enums.Reason;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.models.user.gang.Gang;
import net.artux.pdanetwork.models.user.gang.GangRelationDto;

import java.time.Instant;
import java.util.UUID;

@Data
public class Profile {

    private UUID id;
    private String login;
    private String name;
    private String nickname;
    private Role role;
    private Reason blocked;
    private Gang gang;
    private String avatar;
    private long pdaId;
    private int xp;
    private int friends;
    private int subs;
    private int achievements;
    private Instant registration;
    private FriendRelation friendRelation;
    private GangRelationDto relations;

}
