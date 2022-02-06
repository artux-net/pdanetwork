package net.artux.pdanetwork.models.profile;

import lombok.Data;
import net.artux.pdanetwork.models.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

@Data
@Entity
@Table(name = "note")
public class NoteEntity extends BaseEntity {

    public String title;
    public String content;
    public long time;

    public NoteEntity() {
    }

    public NoteEntity(String title) {
        this.title = title;
        time = Instant.now().toEpochMilli();
    }

    public NoteEntity(String title, String content) {
        this.title = title;
        this.content = content;
        time = Instant.now().toEpochMilli();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public void editContent(String content) {
        this.content = content;
        time = Instant.now().toEpochMilli();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
