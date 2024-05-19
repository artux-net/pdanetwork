package net.artux.pdanetwork.entity.feed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.artux.pdanetwork.entity.BaseEntity;
import net.artux.pdanetwork.entity.user.UserEntity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@ToString(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "user_comment")
public class CommentEntity extends BaseEntity {

    private Instant published;

    @NotBlank(message = "Содержимое не может быть пустым")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity author;

    @JsonIgnore
    @OneToMany(mappedBy = "comment", fetch = FetchType.EAGER)
    private Set<CommentLikeEntity> likes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_comment",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private PostEntity post;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "article_comment",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id"))
    private ArticleEntity article;

    public CommentEntity() {
        setPublished(Instant.now());
        likes = new HashSet<>();
    }

}
