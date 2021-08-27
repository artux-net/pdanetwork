package net.artux.pdanetwork.models;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.authentication.Member;

@Data
@Getter
@RequiredArgsConstructor
public class UserInfo {

    private final String login;
    private final int pdaId;
    private final int group;
    private final String avatar;
    private final String location;
    private final int xp;
    private final Long registration;

}
