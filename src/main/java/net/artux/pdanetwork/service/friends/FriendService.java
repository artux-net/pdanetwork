package net.artux.pdanetwork.service.friends;

import net.artux.pdanetwork.models.FriendModel;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.profile.Achievement;

import java.util.List;

public interface FriendService {

  List<FriendModel> getFriends();
  List<FriendModel> getFriends(Integer pdaId);

  List<FriendModel> getFriendRequests();

  List<FriendModel> getSubs();
  List<FriendModel> getSubs(Integer pdaId);

  Status addFriend(Integer pdaId);

}
