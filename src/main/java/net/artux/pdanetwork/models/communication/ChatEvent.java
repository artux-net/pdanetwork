package net.artux.pdanetwork.models.communication;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatEvent {
    private String content;

    public static ChatEvent of(String content) {
        ChatEvent chatEvent = new ChatEvent();
        chatEvent.setContent(content);
        return chatEvent;
    }
}
