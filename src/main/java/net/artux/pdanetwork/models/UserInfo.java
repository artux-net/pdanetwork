package net.artux.pdanetwork.models;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.models.gang.Gang;

@Data
@RequiredArgsConstructor
public class UserInfo {

    private final String login;
    private final int pdaId;
    private final Gang gang;
    private final String avatar;
    private final int xp;
    private final int money;
    private final Long registration;

}
