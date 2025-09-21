package net.artux.pdanetwork.models.communication;

import lombok.Data;
import net.artux.pdanetwork.models.user.dto.UserDto;

import java.time.Instant;
import java.util.UUID;

@Data
public class MessageDTO {

    private UUID id;
    private Type type;
    private UserDto author;
    private String content;
    private Instant timestamp;

    public MessageDTO(UserDto author, String content) {
        id = UUID.randomUUID();
        type = Type.NEW;
        this.author = author;
        this.content = content;
        timestamp = Instant.now();
    }

    public enum Type {
        OLD,
        NEW,
        UPDATE,
        DELETE
    }

}
