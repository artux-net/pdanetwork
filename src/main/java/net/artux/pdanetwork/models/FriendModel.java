package net.artux.pdanetwork.models;

import lombok.Data;

@Data
public class FriendModel {

    private int pdaId;
    private int group;
    private String login;
    private String avatar;
}
