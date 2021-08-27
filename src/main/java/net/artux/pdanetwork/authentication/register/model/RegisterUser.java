package net.artux.pdanetwork.authentication.register.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
@Getter
@Setter
public class RegisterUser {

    private String login;
    private String password;
    private String email;
    private String name;
    private String nickname;
    private String avatar;

}

