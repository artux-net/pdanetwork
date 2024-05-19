package net.artux.pdanetwork.models.user.ban;

import lombok.Data;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;

import java.time.Instant;
import java.util.UUID;

@Data
public class BanDto {

    private UUID id;
    private SimpleUserDto by;
    private String reason;
    private String message;
    private int seconds;
    private Instant timestamp;

}
