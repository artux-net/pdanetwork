package net.artux.pdanetwork.models.communication;

import lombok.Getter;
import lombok.Setter;
import net.artux.pdanetwork.models.BaseEntity;
import net.artux.pdanetwork.models.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "message")
public class MessageEntity extends BaseEntity {

    @ManyToOne
    private UserEntity author;
    private String content;
    private long timestamp;

    public MessageEntity() {
    }

    public MessageEntity(UserEntity userEntity, String content) {
        this.author = userEntity;
        this.content = content.trim();
        timestamp = Instant.now().toEpochMilli();
    }

    public static MessageEntity getSystemMessage(String loc, String message) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.content = message;

        messageEntity.timestamp = Instant.now().toEpochMilli();
        return messageEntity;
        //TODO: different locales
    }
}
