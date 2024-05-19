package net.artux.pdanetwork.models.feed;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class CommentDto {

    private UUID id;
    private String content;
    private SimpleUserDto author;
    private int likes;

}
