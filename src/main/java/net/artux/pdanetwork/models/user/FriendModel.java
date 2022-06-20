package net.artux.pdanetwork.models.user;

import lombok.Data;
import net.artux.pdanetwork.models.gang.Gang;

@Data
public class FriendModel {

    private int pdaId;
    private Gang gang;
    private String login;
    private String avatar;
}
