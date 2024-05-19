package net.artux.pdanetwork.models.communication;

import lombok.Data;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;

import java.util.List;
import java.util.UUID;

@Data
public class ConversationDTO {

    private UUID id;
    private List<SimpleUserDto> members;
    private String title;
    private MessageDTO lastMessage;
    private String icon;
    private ConversationType type;
    private boolean read;

}
