package net.artux.pdanetwork.models.user.dto;

import net.artux.pdanetwork.models.user.gang.Gang;

import java.time.Instant;
import java.util.UUID;

public record SimpleUserDto(
        UUID id,
        String login,
        String name,
        String nickname,
        String avatar,
        Long pdaId,
        Integer xp,
        Integer achievements,
        Gang gang,
        Instant registration,
        Instant lastLoginAt
){}
