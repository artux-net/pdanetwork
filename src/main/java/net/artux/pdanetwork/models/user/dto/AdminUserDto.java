package net.artux.pdanetwork.models.user.dto;

import lombok.Data;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.models.user.gang.Gang;

import java.time.Instant;
import java.util.UUID;

@Data
public class AdminUserDto {

    private UUID id;
    private String login;
    private String email;
    private String name;
    private String nickname;
    private String avatar;
    private Long pdaId;
    private Role role;
    private Gang gang;
    private int xp;
    private Instant registration;
    private Instant lastLoginAt;
    private boolean chatBan;

}

