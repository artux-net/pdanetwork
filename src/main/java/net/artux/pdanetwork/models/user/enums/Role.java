package net.artux.pdanetwork.models.user.enums;

public enum Role {

    ADMIN(10),
    USER(0),
    TESTER(1),
    MODERATOR(2);

    private final int priority;

    Role(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
