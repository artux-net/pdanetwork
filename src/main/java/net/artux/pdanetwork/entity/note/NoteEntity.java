package net.artux.pdanetwork.entity.note;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;
import net.artux.pdanetwork.entity.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user_note")
@NoArgsConstructor
public class NoteEntity extends BaseEntity {

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private Instant time;
    @ManyToOne
    private UserEntity author;

    public NoteEntity(String title) {
        this.title = title;
        time = Instant.now();
    }

    public NoteEntity(String title, String content) {
        this.title = title;
        this.content = content;
        time = Instant.now();
    }

    public void setContent(String content) {
        this.content = content;
        time = Instant.now();
    }

}
