package net.artux.pdanetwork.models.quest.workflow;

import java.util.Map;

public record Trigger(String event_type, Map client_payload) {
}

