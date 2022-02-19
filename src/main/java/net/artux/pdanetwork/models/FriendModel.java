package net.artux.pdanetwork.models;

import lombok.Data;
import net.artux.pdanetwork.models.user.Group;

@Data
public class FriendModel {

    private int pdaId;
    private Group group;
    private String login;
    private String avatar;
}
