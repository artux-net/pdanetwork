package net.artux.pdanetwork.models.achievement;

import java.util.HashSet;
import java.util.Map;

public record AchievementCreateDto(
        String title,
        String name,
        String description,
        String image,
        AchievementGroup type,
        Map<String, HashSet<String>> condition
) {
}
