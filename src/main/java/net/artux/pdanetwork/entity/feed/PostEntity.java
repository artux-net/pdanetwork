package net.artux.pdanetwork.entity.feed;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.artux.pdanetwork.entity.CommentableEntity;
import net.artux.pdanetwork.entity.user.UserEntity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@ToString(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "user_post")
public class PostEntity extends CommentableEntity {

    @NotBlank(message = "Заголовок не может быть пустым")
    private String title;
    private Instant published;

    @NotBlank(message = "Содержимое не может быть пустым")
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private UserEntity author;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_like",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserEntity> likes;

    public PostEntity() {
        super();
        likes = new HashSet<>();
        setPublished(Instant.now());
    }

    public boolean like(UserEntity user) {
        if (!likes.add(user)) {
            likes.remove(user);
            return false;
        }
        return true;
    }
}
