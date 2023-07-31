package net.artux.pdanetwork.models.achievement;

import java.util.Collection;
import java.util.UUID;

public record AchCategoryDto(
        UUID id,
        String title,
        String name,
        String description,
        String image,
        Collection<AchDto> achievements
) {
}
