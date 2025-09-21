package net.artux.pdanetwork.entity.note;

import jakarta.persistence.FetchType;
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
import java.util.UUID;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity author;

    public NoteEntity(String title) {
        this.title = title;
        time = Instant.now();
    }

    public NoteEntity(UUID id, String title, String content) {
        super(id);
        this.title = title;
        this.content = content;
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
