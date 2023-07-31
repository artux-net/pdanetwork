package net.artux.pdanetwork.models.achievement;

import java.util.UUID;

public record AchDto(
        UUID id,
        String title,
        String name,
        String description,
        String image
) {
}
