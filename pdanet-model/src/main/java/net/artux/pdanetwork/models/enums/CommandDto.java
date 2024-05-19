package net.artux.pdanetwork.models.enums;

import java.util.Set;

public record CommandDto(String id, String title, Set<String> commands) {
}
