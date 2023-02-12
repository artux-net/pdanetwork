package net.artux.pdanetwork.controller.rest.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.ban.BanDto;
import net.artux.pdanetwork.service.user.ban.BanService;
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

    @PostMapping("/{id}")
    public BanDto banUser(@PathVariable("id") UUID uuid, String reason, String message, int secs) {
        return banService.applyBan(uuid, reason, message, secs);
    }

    @PostMapping("/{id}/set/always")
    public boolean alwaysChatBanUser(@PathVariable("id") UUID uuid) {
        return banService.setChatBan(uuid);
    }

    @GetMapping("/{id}/all")
    public List<BanDto> getBans(@PathVariable("id") UUID uuid) {
        return banService.getBans(uuid);
    }

    @GetMapping("/{id}/current")
    public BanDto getCurrentBan(@PathVariable("id") UUID uuid) {
        return banService.getCurrentBan(uuid);
    }

}
