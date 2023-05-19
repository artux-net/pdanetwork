package net.artux.pdanetwork.models.user.enums;

import net.artux.pdanetwork.models.enums.EnumGetter;

public enum Role implements EnumGetter {

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

    @Override
    public String getId() {
        return this.name();
    }

    public String getTitle() {
        return title;
    }
}
