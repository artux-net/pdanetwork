package net.artux.pdanetwork.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FriendModel {

    private int pdaId;
    private int group;
    private String login;
    private String avatar;
}
