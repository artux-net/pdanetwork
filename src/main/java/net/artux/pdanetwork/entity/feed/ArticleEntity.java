package net.artux.pdanetwork.entity.feed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.artux.pdanetwork.entity.CommentableEntity;
import org.hibernate.annotations.Type;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@ToString(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "article")
public class ArticleEntity extends CommentableEntity {

    @NotBlank(message = "Заголовок не может быть пустым")
    private String title;
    private String image;

    @NotBlank(message = "Описание не может быть пустым")
    @Size(max = 250, message = "Описание слишком большое")
    private String description;
    private Instant published;

    @NotBlank(message = "Содержимое не может быть пустым")
    @Column(columnDefinition = "text")
    private String content;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL, CascadeType.MERGE})
    @JoinTable(
            name = "article_tag",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<TagEntity> tags;

    @JsonIgnore
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, orphanRemoval = true)
    public Set<ArticleLikeEntity> likes;

    private Integer views = 0;

    public ArticleEntity() {
        setPublished(Instant.now());
        likes = new HashSet<>();
        comments = new HashSet<>();
        tags = new HashSet<>();
    }

}
