package net.artux.pdanetwork.models.user.dto;

import lombok.Data;
import net.artux.pdanetwork.models.user.gang.Gang;

import java.time.Instant;
import java.util.UUID;

@Data
public class SimpleUserDto {

    private UUID id;
    private String login;
    private String name;
    private String nickname;
    private String avatar;
    private Long pdaId;
    private int xp;
    private int achievements;
    private Gang gang;
    private Instant registration;

}
