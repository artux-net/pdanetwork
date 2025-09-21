package net.artux.pdanetwork.models.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.models.user.gang.Gang;

@Data
public class AdminEditUserDto {

    @NotBlank
    private String login;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String nickname;
    @NotBlank
    private String avatar;
    @NotNull
    private Role role;
    @NotNull
    private Gang gang;
    private Boolean chatBan;

}

