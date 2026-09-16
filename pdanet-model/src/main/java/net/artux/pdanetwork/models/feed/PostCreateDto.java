package net.artux.pdanetwork.models.feed;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostCreateDto {

    @NotBlank(message = "{validation.title.blank}")
    private String title;
    @NotBlank(message = "{validation.content.blank}")
    private String content;

}
