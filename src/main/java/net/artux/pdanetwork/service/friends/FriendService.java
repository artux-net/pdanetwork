package net.artux.pdanetwork.service.friends;

import net.artux.pdanetwork.models.FriendModel;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.profile.Achievement;

import java.util.List;

public interface FriendService {



  List<FriendModel> getFriends(Integer pdaId);
  List<FriendModel> getFriendRequests(Integer pdaId);
  List<FriendModel> getFriends();
  List<FriendModel> getFriendRequests();
  Status addFriend(Integer pdaId);
  Status requestFriend(Integer pdaId);
  Status removeFriend(Integer pdaId);

}
