package net.artux.pdanetwork.models.user.dto;

import lombok.Data;
import net.artux.pdanetwork.models.user.enums.Role;

@Data
public class AdminEditUserDto {

    private Role role;

}

