package net.artux.pdanetwork.models.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserDto {

    private String login;
    private String password;
    private String email;
    private String name;
    private String nickname;
    private String avatar;

    public RegisterUserDto(String login, String password, String email, String name, String nickname, String avatar) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.avatar = avatar;
    }
}

