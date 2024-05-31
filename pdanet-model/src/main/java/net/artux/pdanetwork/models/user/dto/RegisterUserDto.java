package net.artux.pdanetwork.models.user.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {

    //TODO validation
    private String login;
    private String password;
    private String email;
    private String name;
    private String nickname;
    private String avatar;
}

