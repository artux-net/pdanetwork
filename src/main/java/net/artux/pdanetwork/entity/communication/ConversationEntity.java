package net.artux.pdanetwork.entity.communication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;
import net.artux.pdanetwork.entity.user.UserEntity;

import jakarta.persistence.*;
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

    @OneToMany(fetch = FetchType.LAZY)
    private List<MessageEntity> messages;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<UserEntity> members = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity owner;

    private Instant time;
    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        GROUP,
        PRIVATE
    }
}
