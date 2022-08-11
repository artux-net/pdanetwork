package net.artux.pdanetwork.models.communication;

import lombok.Data;
import net.artux.pdanetwork.models.user.dto.UserDto;

import java.time.Instant;

@Data
public class MessageDTO {

    private UserDto author;
    private String content;
    private Instant timestamp;

}
