package net.artux.pdanetwork.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;
import net.artux.pdanetwork.service.friends.FriendService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Друзья")
@RequestMapping("/friends")
public class FriendsController {

    private final FriendService friendService;

    @Operation(summary = "Получить друзей по pdaId")
    @GetMapping("/{id}")
    public List<SimpleUserDto> getFriends(@PathVariable("id") UUID id) {
        return friendService.getFriends(id);
    }

    @Operation(summary = "Получить друзей")
    @GetMapping
    public List<SimpleUserDto> getFriends() {
        return friendService.getFriends();
    }

    @Operation(summary = "Получить запросы дружбы")
    @GetMapping("/requests")
    public List<SimpleUserDto> getFriendsRequests() {
        return friendService.getFriendRequests();
    }

    @Operation(summary = "Получить подписчиков")
    @GetMapping("/subs")
    public List<SimpleUserDto> getSubs() {
        return friendService.getSubs();
    }

    @Operation(summary = "Получить подписчиков по pdaId")
    @GetMapping("/subs/{id}")
    public List<SimpleUserDto> getSubs(@PathVariable("id") UUID id) {
        return friendService.getSubs(id);
    }

    @Operation(summary = "Запросить/добавить/удалить друга")
    @PostMapping
    public Status addFriend(@RequestParam("pdaId") UUID pdaId) {
        return friendService.addFriend(pdaId);
    }
}