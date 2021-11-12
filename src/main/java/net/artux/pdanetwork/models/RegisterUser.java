package net.artux.pdanetwork.models;

import lombok.Data;

@Data
public class RegisterUser {

    private String login;
    private String password;
    private String email;
    private String name;
    private String nickname;
    private String avatar;

}

