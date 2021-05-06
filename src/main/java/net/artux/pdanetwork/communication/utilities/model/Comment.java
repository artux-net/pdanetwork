package net.artux.pdanetwork.communication.utilities.model;

public class Comment {

    private int postId;
    private int pdaId;
    private final String message;

    public Comment(int pdaId, String message) {
        this.pdaId = pdaId;
        this.message = message;
    }

    public int getPostId() {
        return postId;
    }

    public int getPdaId() {
        return pdaId;
    }

    public void setPdaId(int pdaId) {
        this.pdaId = pdaId;
    }

    public String getMessage() {
        return message;
    }
}
