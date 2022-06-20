package net.artux.pdanetwork.models.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.models.gang.GangRelationDto;
import net.artux.pdanetwork.models.gang.Gang;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private UUID uid;
    private String login;
    private String email;
    private String name;
    private String nickname;
    private String avatar;
    private Long pdaId;
    private String role;
    private Gang gang;
    private int xp;
    private int money;
    public GangRelationDto relations;
    private Long registration;
    private Long lastLoginAt;

}
