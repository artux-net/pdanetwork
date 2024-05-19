package net.artux.pdanetwork.models.command;

import net.artux.pdanetwork.models.enums.EnumGetter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ClientCommand implements EnumGetter {

    NOTIFICATION("Показать уведомление", Stream.of("showNotification", "openNotification")),
    OPEN_SELLER("Открыть продавца", Stream.of("openSeller")),
    OPEN_STAGE("Открыть стадию", Stream.of("openStage")),
    LOOP_MUSIC("Повторять музыку", Stream.of("loopMusic")),
    PLAY_MUSIC("Проиграть музыку", Stream.of("playMusic")),
    PLAY_SOUND("Проиграть звук", Stream.of("playSound")),
    PAUSE_SOUND("Остановить звук", Stream.of("pauseSound")),
    STOP_MUSIC("Остановить музыку", Stream.of("stopMusic")),
    PAUSE_ALL_SOUND("Остановить звуки и музыку", Stream.of("pauseAllSound")),
    RESUME_ALL_SOUND("Продолжить звуки и музыку", Stream.of("resumeAllSound")),
    SHOW_AD("Показать рекламу", Stream.of("showAd")),
    SYNC_NOW("Синхронизация (техническая)", Stream.of("syncNow")),
    SCRIPT("Скрипт", Stream.of("script", "lua"));

    private final String title;
    private final Set<String> commands;

    ClientCommand(String title, Stream<String> commands) {
        this.title = title;
        this.commands = commands.collect(Collectors.toSet());
    }

    public Set<String> getCommands() {
        return commands;
    }

    public static ClientCommand of(String c) {
        for (ClientCommand cmd : values()) {
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
