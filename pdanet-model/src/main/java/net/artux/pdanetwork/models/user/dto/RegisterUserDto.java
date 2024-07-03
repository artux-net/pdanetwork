package net.artux.pdanetwork.models.user.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {

    private String email;
    private String password;
    private String nickname;
    private String avatar;
}

