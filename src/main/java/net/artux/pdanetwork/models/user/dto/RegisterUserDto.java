package net.artux.pdanetwork.models.user.dto;

import lombok.Data;

@Data
public class RegisterUserDto {

    private String login;
    private String password;
    private String email;
    private String name;
    private String nickname;
    private String avatar;

}

