package net.artux.pdanetwork.models.communication;

import lombok.Data;
import net.artux.pdanetwork.entity.communication.ConversationEntity;

import java.util.List;
import java.util.UUID;

@Data
public class ConversationCreateDTO {
    private String title;
    private String icon;
    private List<UUID> users;
    private ConversationEntity.Type type;
}
