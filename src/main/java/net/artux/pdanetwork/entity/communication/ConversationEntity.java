package net.artux.pdanetwork.entity.communication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;
import net.artux.pdanetwork.entity.user.UserEntity;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "user_conversation")
public class ConversationEntity extends BaseEntity {

    private String title;
    private String icon;
    @ManyToMany
    private List<MessageEntity> messages;
    @ManyToMany
    private Set<UserEntity> members = new HashSet<>();
    @OneToOne
    private MessageEntity lastMessage;
    @OneToOne
    private UserEntity owner;
    private Instant time;
    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        GROUP,
        PRIVATE
    }

}
