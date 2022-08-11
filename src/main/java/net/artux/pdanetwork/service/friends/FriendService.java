package net.artux.pdanetwork.service.friends;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.FriendModel;

import java.util.List;
import java.util.UUID;

public interface FriendService {

  List<FriendModel> getFriends();
  List<FriendModel> getFriends(UUID pdaId);

  List<FriendModel> getFriendRequests();

  List<FriendModel> getSubs();
  List<FriendModel> getSubs(UUID pdaId);

  Status addFriend(UUID pdaId);

}
