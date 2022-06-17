package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.FriendModel;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.service.friends.FriendService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "Профиль - Друзья")
@RequestMapping("/profile/friends")
public class FriendsController {

    private final FriendService friendService;

    @ApiOperation(value = "Получить друзей по pdaId")
    @GetMapping("/{id}")
    public List<FriendModel> getFriends(@PathVariable("id") Long id) {
        return friendService.getFriends(id);
    }

    @ApiOperation(value = "Получить друзей")
    @GetMapping
    public List<FriendModel> getFriends() {
        return friendService.getFriends();
    }

    @ApiOperation(value = "Получить запросы дружбы")
    @GetMapping("/requests")
    public List<FriendModel> getFriendsRequests() {
        return friendService.getFriendRequests();
    }

    @ApiOperation(value = "Получить подписчиков")
    @GetMapping("/subs")
    public List<FriendModel> getSubs() {
        return friendService.getSubs();
    }

    @ApiOperation(value = "Получить подписчиков по pdaId")
    @GetMapping("/subs/{id}")
    public List<FriendModel> getSubs(@PathVariable("id") Long id) {
        return friendService.getSubs(id);
    }

    @ApiOperation(value = "Запросить/добавить/удалить друга")
    @PostMapping
    public Status addFriend(@RequestParam("pdaId") long pdaId) {
        return friendService.addFriend(pdaId);
    }
}