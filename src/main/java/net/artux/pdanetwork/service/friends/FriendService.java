package net.artux.pdanetwork.service.friends;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.UserRelation;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;

import java.util.List;
import java.util.UUID;

public interface FriendService {

  List<SimpleUserDto> getRelatedUsers(UUID pdaId, UserRelation relation);

  Status relateUser(UUID pdaId);

  List<SimpleUserDto> getFriendRequests();

}
