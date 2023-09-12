package net.artux.pdanetwork.models.achievement;

import java.util.Collection;

public record AchCategoryDto(
        String name,
        String title,
        String description,
        String image,
        Collection<AchDto> achievements
) {
}
