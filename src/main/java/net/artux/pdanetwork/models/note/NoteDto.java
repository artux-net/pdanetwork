package net.artux.pdanetwork.models.note;

import lombok.Getter;
import lombok.Setter;
import net.artux.pdanetwork.models.BaseEntity;
import net.artux.pdanetwork.models.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Getter
@Setter
public class NoteDto extends BaseEntity {

    private String title;
    private String content;
    private long time;

    public NoteDto() {
    }

    public NoteDto(String title) {
        this.title = title;
        time = Instant.now().toEpochMilli();
    }

    public NoteDto(String title, String content) {
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
