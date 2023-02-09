package net.artux.pdanetwork.entity.communication;

import lombok.Getter;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;
import net.artux.pdanetwork.entity.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_message")
public class MessageEntity extends BaseEntity {

    @ManyToOne
    private UserEntity author;
    private String content;
    private Instant timestamp;
    @ManyToOne
    private ConversationEntity conversation;

    public MessageEntity() {
    }

    public MessageEntity(UserEntity userEntity, String content) {
        id = UUID.randomUUID();
        this.author = userEntity;
        this.content = content.trim();
        timestamp = Instant.now();
    }

    public static MessageEntity getSystemMessage(String loc, String message) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.content = message;

        messageEntity.timestamp = Instant.now();
        return messageEntity;
        //TODO: different locales
    }
}
