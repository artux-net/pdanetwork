package net.artux.pdanetwork.models.feed;

import net.artux.pdanetwork.models.user.dto.SimpleUserDto;

import java.util.UUID;

public record CommentDto(
        UUID id,
        String content,
        SimpleUserDto author,
        int likes
){}
