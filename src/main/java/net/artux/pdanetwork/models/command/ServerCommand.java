package net.artux.pdanetwork.models.command;

import net.artux.pdanetwork.models.enums.EnumGetter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ServerCommand implements EnumGetter {

    ADD("Добавление предмета, параметра, отношения", Stream.of("add", "+")),
    REMOVE("Удаление", Stream.of("remove", "-")),
    SET_VALUE("Установка параметра", Stream.of("=", "setValue")),
    MULTIPLY_VALUE("Умножение параметра", Stream.of("*", "multiply")),
    SET_MONEY("Задание денег", Stream.of("setMoney", "money")),
    SET_ITEM_QUANTITY("Задание количества предмета", Stream.of("setItem", "item")),
    SET_ITEM_CONDITION("Задание состояния предмета", Stream.of("item_condition", "setItemCondition", "itemCondition")),
    SET_XP("Задание опыта", Stream.of("xp", "setXp")),
    SET_MAIN_ITEM("Установка предмета как основного", Stream.of("set", "setMainItem")),
    ADD_NOTE("Добавить заметку", Stream.of("note")),
    ACHIEVE("Добавить достижение", Stream.of("achieve")),
    RESET("Сброс", Stream.of("reset")),
    SET_RELATION("Установка отношения", Stream.of("relation", "setRelation")),
    ADD_RELATION("Добавление отношения", Stream.of("addRelation", "calculateRelation")),
    EXIT_STORY("Выйти из истории", Stream.of("exitStory")),
    FINISH_STORY("Закончить историю", Stream.of("finishStory")),
    STATE("Задание этапа прохождения (техническая)", Stream.of("state"));

    private final String title;
    private final Set<String> commands;

    ServerCommand(String title, Stream<String> commands) {
        this.title = title;
        this.commands = commands.collect(Collectors.toSet());
    }

    public Set<String> getCommands() {
        return commands;
    }

    public String getCommand() {
        return commands.iterator().next();
    }

    public static ServerCommand of(String c) {
        for (ServerCommand cmd : values()) {
            if (cmd.commands.contains(c)) return cmd;
        }
        return null;
    }

    @Override
    public String getId() {
        return name();
    }

    @Override
    public String getTitle() {
        return title;
    }
}
