package net.artux.pdanetwork.models.user.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.gang.Gang;

@Data
@RequiredArgsConstructor
public class UserInfoDto {

    private final String login;
    private final int pdaId;
    private final Gang gang;
    private final String avatar;
    private final int xp;
    private final int money;
    private final Long registration;

}
