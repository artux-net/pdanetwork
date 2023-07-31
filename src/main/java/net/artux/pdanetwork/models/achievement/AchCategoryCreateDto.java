package net.artux.pdanetwork.models.achievement;

public record AchCategoryCreateDto(
        String title,
        String name,
        String description,
        String image
) {
}
