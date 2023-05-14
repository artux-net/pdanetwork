package net.artux.pdanetwork.models.user.enums;

import java.util.Arrays;

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

    public static String[] getRoles(Role role){
        return Arrays.stream(Role.values()).filter(r -> r.getPriority() <= role.getPriority()).map(Role::name).toArray(String[]::new);
    }
}
