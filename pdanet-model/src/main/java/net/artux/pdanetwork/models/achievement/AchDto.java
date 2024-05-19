package net.artux.pdanetwork.models.achievement;

import java.util.UUID;

public record AchDto(
        UUID id,
        String name,
        String title,
        String description,
        String image
) {
}
