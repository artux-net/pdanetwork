package net.artux.pdanetwork.models;

import lombok.Data;

@Data
public class UserInfo {

    private final String login;
    private final int pdaId;
    private final int group;
    private final String avatar;
    private final String location;
    private final int xp;
    private final Long registration;

}
