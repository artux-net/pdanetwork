package net.artux.pdanetwork.models.feed;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ArticleDto {

    private UUID id;
    private String title;
    private String image;
    private List<String> tags;
    private String description;
    private String url;
    private Instant published;

}
