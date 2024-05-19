package net.artux.pdanetwork.models.user.enums;

import net.artux.pdanetwork.models.enums.EnumGetter;

public enum Role implements EnumGetter {

    ADMIN(10, "Администратор"),
    MODERATOR(3, "Модератор"),
    CREATOR(2, "Писатель"),
    TESTER(1, "Тестер"),
    USER(0, "Обычный пользователь");

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
