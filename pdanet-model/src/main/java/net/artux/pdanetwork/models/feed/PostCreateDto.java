package net.artux.pdanetwork.models.feed;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostCreateDto {

    @NotBlank(message = "Заголовок не может быть пустым")
    private String title;
    @NotBlank(message = "Содержимое не может быть пустым")
    private String content;

}
