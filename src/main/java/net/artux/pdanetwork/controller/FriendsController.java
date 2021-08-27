package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.FriendModel;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.ResponsePage;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.service.feed.FeedService;
import net.artux.pdanetwork.service.friends.FriendService;
import net.artux.pdanetwork.servlets.Feed.Models.Article;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "Друзья")
@RequestMapping("/friends")
public class FriendsController {

  private final FriendService friendService;

  @ApiOperation(value = "Получить друзей по pdaId")
  @GetMapping("/{id}")
  public List<FriendModel> getFriends(@PathParam("id") Integer pdaId){
    return friendService.getFriends(pdaId);
  }

  @ApiOperation(value = "Получить друзей")
  @GetMapping
  public List<FriendModel> getFriends(){
    return friendService.getFriends();
  }

  @ApiOperation(value = "Получить запросы дружбы")
  @GetMapping("/requests")
  public List<FriendModel> getFriendsRequests(){
    return friendService.getFriendRequests();
  }

  @ApiOperation(value = "Получить запросы дружбы по pdaId")
  @GetMapping("/requests/{id}")
  public List<FriendModel> getFriendsRequests(@PathParam("id") Integer pdaId){
    return friendService.getFriendRequests(pdaId);
  }

  @ApiOperation(value = "Добавить в друзья по запросу")
  @PostMapping("/add")
  public Status addFriend(@QueryParam("pdaId") Integer pdaId){
    return friendService.addFriend(pdaId);
  }

  @ApiOperation(value = "Запрос дружбы")
  @PostMapping("/requests")
  public Status requestFriend(@QueryParam("pdaId") Integer pdaId){
    return friendService.requestFriend(pdaId);
  }

  @ApiOperation(value = "Удалить с друзей/запросов")
  @PostMapping("/remove")
  public Status removeFriend(@QueryParam("pdaId") Integer pdaId){
    return friendService.removeFriend(pdaId);
  }
}