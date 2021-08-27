package net.artux.pdanetwork.service.friends;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.models.FriendModel;
import net.artux.pdanetwork.models.MemberMapper;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.service.member.MemberService;
import net.artux.pdanetwork.service.files.AchievementsService;
import net.artux.pdanetwork.utills.ServletContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendServiceIml implements FriendService {

  private final MemberService memberService;
  private final MemberMapper memberMapper;
  private final AchievementsService achievementsService;


  @Override
  public List<FriendModel> getFriends(Integer pdaId) {
    List<FriendModel> friendModels = new ArrayList<>();
    List<Integer> list = memberService.getMemberByPdaId(pdaId).getFriends();
    for (int id : list) {
      if (ServletContext.mongoUsers.getFriends(id).contains(pdaId))
        friendModels.add(new FriendModel(ServletContext.mongoUsers.getProfileByPdaId(id)));
    }
    return friendModels;
  }

  @Override
  public List<FriendModel> getFriendRequests(Integer pdaId) {
    List<FriendModel> friendModels = new ArrayList<>();
    List<Integer> list = memberService.getMemberByPdaId(pdaId).getFriendRequests();
    for (int id : list) {
        friendModels.add(new FriendModel(ServletContext.mongoUsers.getProfileByPdaId(id)));
    }
    return friendModels;
  }

  @Override
  public List<FriendModel> getFriends() {
    return getFriends(memberService.getMember().getPdaId());
  }

  @Override
  public List<FriendModel> getFriendRequests() {
    return getFriendRequests(memberService.getMember().getPdaId());
  }

  @Override
  public Status addFriend(Integer pdaId) {
    Member user = memberService.getMember();
    if (user.friendRequests.contains(pdaId)) {
      user.friendRequests.remove(pdaId);
      if (!user.friends.contains(pdaId)) {
        user.friends.add(pdaId);

        memberService.saveMember(user);
      }
      return new Status(true, "Ok");
    }else
      return new Status(false, "Failed");
  }

  @Override
  public Status requestFriend(Integer pdaId) {
    Member user = memberService.getMember();
    Member newFriend = memberService.getMemberByPdaId(pdaId);

    user.friends.add(newFriend.getPdaId());
    memberService.saveMember(user);

    newFriend.friendRequests.add(user.getPdaId());
    memberService.saveMember(newFriend);
    return new Status(true, "Ok");
  }

  @Override
  public Status removeFriend(Integer pdaId) {
    Member user = memberService.getMember();
    if (user.friends.contains(pdaId)) {
      user.friends.remove(pdaId);
      memberService.saveMember(user);

      Member oldFriend = memberService.getMemberByPdaId(pdaId);
      if (oldFriend.friends.contains(user.getPdaId())) {
        user.friendRequests.add(pdaId);
        memberService.saveMember(user);
      } else {
        oldFriend.friendRequests.remove(user.getPdaId());
        memberService.saveMember(oldFriend);
      }
      return new Status(true, "Ok");
    }else
      return new Status(false, "Failed");
  }
}
