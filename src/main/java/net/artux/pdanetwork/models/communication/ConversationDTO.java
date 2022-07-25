package net.artux.pdanetwork.models.communication;

import lombok.Data;
import net.artux.pdanetwork.entity.conversation.ConversationEntity;
import net.artux.pdanetwork.models.user.dto.UserDto;

import java.util.List;

@Data
public class ConversationDTO {

    private long id;
    private List<UserDto> members;
    private String title;
    private MessageDTO lastMessage;
    private String icon;
    private ConversationEntity.Type type;
    private boolean read;

}
