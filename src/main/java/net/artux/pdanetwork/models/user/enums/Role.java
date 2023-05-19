package net.artux.pdanetwork.models.user.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Role {

    ADMIN(10, "Администратор"),
    USER(0, "Обычный пользователь"),
    TESTER(1, "Тестер"),
    MODERATOR(2, "Модератор");

    private final int priority;
    private final String title;

    Role(int priority, String title) {
        this.priority = priority;
        this.title = title;
    }

    public int getPriority() {
        return priority;
    }

    public String getTitle() {
        return title;
    }
}
