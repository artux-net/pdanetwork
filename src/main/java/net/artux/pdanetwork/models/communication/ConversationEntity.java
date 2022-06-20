package net.artux.pdanetwork.models.communication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.models.BaseEntity;
import net.artux.pdanetwork.models.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "conversation")
public class ConversationEntity extends BaseEntity {

    private String title;
    @ManyToMany
    private List<MessageEntity> messages;
    @ManyToMany
    private List<UserEntity> members;
    @ManyToOne
    private MessageEntity lastMessage;

    public ConversationEntity(String title, List<UserEntity> userEntities) {
        this.title = title;
        this.members = userEntities;
    }

}
