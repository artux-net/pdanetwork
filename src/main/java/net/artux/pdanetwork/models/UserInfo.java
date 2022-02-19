package net.artux.pdanetwork.models;

import lombok.Data;
import net.artux.pdanetwork.models.user.Group;

@Data
public class UserInfo {

    private final String login;
    private final int pdaId;
    private final Group group;
    private final String avatar;
    private final String location;
    private final int xp;
    private final Long registration;

}
