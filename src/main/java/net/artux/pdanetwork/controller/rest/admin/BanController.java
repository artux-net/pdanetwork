package net.artux.pdanetwork.controller.rest.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.ban.BanDto;
import net.artux.pdanetwork.service.user.ban.BanService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Блокировка пользователей")
@RequestMapping("/utility/bans")
@RequiredArgsConstructor
public class BanController {

    private final BanService banService;

    @PostMapping("/{userId}")
    public BanDto banUser(@PathVariable("userId") UUID uuid, String reason, String message, int secs) {
        return banService.applyBan(uuid, reason, message, secs);
    }

    @PostMapping("/{userId}/clearAll")
    public boolean clearAllBansForUser(@PathVariable("userId") UUID uuid) {
        return banService.clearAllBans(uuid);
    }

    @DeleteMapping("/{banId}")
    public boolean clearBan(@PathVariable("banId") UUID uuid) {
        return banService.clearBan(uuid);
    }

    @PostMapping("/{userId}/set/always")
    public boolean alwaysChatBanUser(@PathVariable("userId") UUID uuid) {
        return banService.setChatBan(uuid);
    }

    @GetMapping("/{userId}/all")
    public List<BanDto> getBans(@PathVariable("userId") UUID uuid) {
        return banService.getBans(uuid);
    }

    @GetMapping("/{userId}/current")
    public BanDto getCurrentBan(@PathVariable("userId") UUID uuid) {
        return banService.getCurrentBan(uuid);
    }

}
