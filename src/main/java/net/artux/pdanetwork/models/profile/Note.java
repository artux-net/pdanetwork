package net.artux.pdanetwork.models.profile;

import java.time.Instant;

public class Note {

    public int cid;
    public String title;
    public String content;
    public long time;

    public Note() {
    }

    public Note(int cid, String title) {
        this.cid = cid;
        this.title = title;
        time = Instant.now().toEpochMilli();
    }

    public Note(int cid, String title, String content) {
        this.cid = cid;
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
