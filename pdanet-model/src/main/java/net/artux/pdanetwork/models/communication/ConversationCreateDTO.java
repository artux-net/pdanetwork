package net.artux.pdanetwork.models.communication;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@lombok.RequiredArgsConstructor
public class ConversationCreateDTO {

    private String title;
    private String icon;
    private List<UUID> users;
    private ConversationType type;

    public ConversationCreateDTO(String title, String icon, List<UUID> users, ConversationType type) {
        this.title = title;
        this.icon = icon;
        this.users = users;
        this.type = type;
    }
}
