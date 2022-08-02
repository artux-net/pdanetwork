package net.artux.pdanetwork.models.communication;

import lombok.Data;
import net.artux.pdanetwork.models.user.gang.Gang;
import net.artux.pdanetwork.entity.user.UserEntity;

import java.time.Instant;

@Data
public class MessageDTO {

    private long id;
    private String login;
    private Gang gang;
    private String avatar;
    private long pdaId;
    private String content;
    private long timestamp;

    public MessageDTO(UserEntity user, String content) {
        id = 0;
        login = user.getLogin();
        gang = user.getGang();
        avatar = user.getAvatar();
        pdaId = user.getPdaId();
        this.content = content;
        timestamp = Instant.now().toEpochMilli();
    }
}
