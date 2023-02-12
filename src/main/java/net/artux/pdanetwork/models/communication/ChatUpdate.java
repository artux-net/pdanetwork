package net.artux.pdanetwork.models.communication;

import lombok.Data;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Data
public class ChatUpdate {

    private List<MessageDTO> updates;
    private List<ChatEvent> events;
    private Instant timestamp;

    public ChatUpdate(List<MessageDTO> updates, List<ChatEvent> events) {
        this.updates = updates;
        this.events = events;
        this.timestamp = Instant.now();
    }

    public static ChatUpdate of(List<MessageDTO> messages) {
        return new ChatUpdate(messages, new LinkedList<>());
    }

    public static ChatUpdate of(MessageDTO message) {
        return new ChatUpdate(Collections.singletonList(message), new LinkedList<>());
    }


    public static ChatUpdate empty() {
        return new ChatUpdate(Collections.emptyList(), Collections.emptyList());
    }

    public void addEvent(ChatEvent event) {
        events.add(event);
    }

    public ChatUpdate asOld() {
        for (MessageDTO message : updates) {
            message.setType(MessageDTO.Type.OLD);
        }
        return this;
    }

}
