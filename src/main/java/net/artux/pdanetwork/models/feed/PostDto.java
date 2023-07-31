package net.artux.pdanetwork.models.feed;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class PostDto {

    private UUID id;
    private String title;
    private String content;
    private int likes;
    private int comments;

}
