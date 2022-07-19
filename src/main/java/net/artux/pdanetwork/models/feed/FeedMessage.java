package net.artux.pdanetwork.models.feed;

import java.util.ArrayList;
import java.util.List;

public class FeedMessage {

    private int id;
    private final String title;
    private final String message;
    private final String icon;
    private final List<Comment> comments = new ArrayList<>();

    public FeedMessage(String title, String message, String icon) {
        this.title = title;
        this.message = message;
        this.icon = icon;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getIcon() {
        return icon;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public List<Comment> getComments() {
        return comments;
    }
}
