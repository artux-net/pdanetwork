package net.artux.pdanetwork.models.feed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
public class ArticleCreateDto {

    @NotBlank(message = "Заголовок не может быть пустым")
    private String title;
    @Pattern(regexp = ".*?(gif|jpeg|png|jpg|bmp)", message = "Неправильный формат ссылки на изображение")
    private String image;
    private Set<String> tags;
    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = 0, max = 250, message = "Описание слишком большое")
    private String description;
    @NotBlank(message = "Содержимое не может быть пустым")
    private String content;

}
