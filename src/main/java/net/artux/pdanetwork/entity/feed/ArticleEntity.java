package net.artux.pdanetwork.entity.feed;

import lombok.Getter;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "article")
public class ArticleEntity extends BaseEntity {

    @NotBlank(message = "Заголовок не может быть пустым")
    private String title;
    @Pattern(regexp = "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)", message = "Неправильный формат ссылки на изображение")
    private String image;
    //private List<String> tags = new HashSet<>();//todo
    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = 0, max = 250, message = "Описание слишком большое")
    private String description;
    private Instant published;

    @NotBlank(message = "Содержимое не может быть пустым")
    @Column(columnDefinition = "TEXT")
    private String content;

    public ArticleEntity() {
        setPublished(Instant.now());
    }

}
