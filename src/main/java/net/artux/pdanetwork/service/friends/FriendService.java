package net.artux.pdanetwork.service.friends;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;

import java.util.List;
import java.util.UUID;

public interface FriendService {

  List<SimpleUserDto> getFriends();
  List<SimpleUserDto> getFriends(UUID pdaId);

  List<SimpleUserDto> getFriendRequests();

  List<SimpleUserDto> getSubs();
  List<SimpleUserDto> getSubs(UUID pdaId);

  Status addFriend(UUID pdaId);

}
