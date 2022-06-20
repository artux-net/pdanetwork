package net.artux.pdanetwork.service.friends;

import net.artux.pdanetwork.models.user.FriendModel;
import net.artux.pdanetwork.models.Status;

import java.util.List;

public interface FriendService {

  List<FriendModel> getFriends();
  List<FriendModel> getFriends(Long pdaId);

  List<FriendModel> getFriendRequests();

  List<FriendModel> getSubs();
  List<FriendModel> getSubs(Long pdaId);

  Status addFriend(Long pdaId);

}
