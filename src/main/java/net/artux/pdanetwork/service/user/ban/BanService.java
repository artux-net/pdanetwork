package net.artux.pdanetwork.service.user.ban;

import net.artux.pdanetwork.models.user.ban.BanDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BanService {

    List<BanDto> getBans(UUID userId);

    boolean isBanned(UUID userId);

    boolean setChatBan(UUID userId);

    BanDto applySystemBan(UUID userId, String reason, String message, int secs);

    BanDto applyBan(UUID userId, String reason, String message, int secs);

    BanDto getCurrentBan(UUID userId);

    boolean clearAllBans(UUID uuid);

    boolean clearBan(UUID banId);

    Map<UUID, BanDto> getCurrentBans();
}
