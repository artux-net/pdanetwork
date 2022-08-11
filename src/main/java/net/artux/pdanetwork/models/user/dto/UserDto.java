package net.artux.pdanetwork.models.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.models.user.gang.Gang;
import net.artux.pdanetwork.models.user.gang.GangRelationDto;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private UUID id;
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
    private GangRelationDto relations;
    private Instant registration;
    private Instant lastLoginAt;

}
