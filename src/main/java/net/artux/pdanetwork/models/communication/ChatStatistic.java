package net.artux.pdanetwork.models.communication;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.ban.BanDto;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ChatStatistic {

    private List<SimpleUserDto> sessions;
    private Map<UUID, BanDto> currentBans;

    public ChatStatistic(List<SimpleUserDto> sessions, Map<UUID, BanDto> currentBans) {
        this.sessions = sessions;
        this.currentBans = currentBans;
    }
}
