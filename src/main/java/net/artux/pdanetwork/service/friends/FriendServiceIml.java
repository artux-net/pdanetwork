package net.artux.pdanetwork.service.friends;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.UserEntity;
import net.artux.pdanetwork.models.FriendModel;
import net.artux.pdanetwork.models.MemberMapper;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.service.member.MemberService;
import net.artux.pdanetwork.service.files.AchievementsService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendServiceIml implements FriendService {

  private final MemberService memberService;
  private final MemberMapper memberMapper;
  private final AchievementsService achievementsService;

  private List<FriendModel> getModels(List<ObjectId> ids){
    List<FriendModel> friendModels = new ArrayList<>();
    for (ObjectId id : ids) {
      friendModels.add(memberMapper.friendModel(memberService.getMember(id)));
    }
    return friendModels;
  }

  @Override
  public List<FriendModel> getFriends(Integer pdaId) {
    return getModels(memberService.getMemberByPdaId(pdaId).getFriends());
  }

  @Override
  public List<FriendModel> getSubs() {
    return getModels(memberService.getMember().getSubs());
  }

  @Override
  public List<FriendModel> getSubs(Integer pdaId) {
    return getModels(memberService.getMemberByPdaId(pdaId).getSubs());
  }

  @Override
  public List<FriendModel> getFriends() {
    return getFriends(memberService.getMember().getPdaId());
  }

  @Override
  public List<FriendModel> getFriendRequests() {
    return getModels(memberService.getMember().getRequests());
  }

  @Override
  @Transactional // TODO everywhere
  public Status addFriend(Integer pdaId) {
    UserEntity user = memberService.getMember();
    UserEntity another = memberService.getMemberByPdaId(pdaId);

    Status status;

    if (!another.get_id().equals(user.get_id())) {
      if (another.subs.contains(user.get_id())) {
        //отношения есть, юзер удаляется из подписчиков
        another.subs.remove(user.get_id());
        user.requests.remove(another.get_id());

        status = new Status(true, "Запрос отменен");
      } else if (another.friends.contains(user.get_id())) {
        //отношения есть, юзер удаляется из друзей
        another.requests.add(user.get_id());
        user.subs.add(another.get_id());


        another.friends.remove(user.get_id());
        user.friends.remove(another.get_id());

        status = new Status(true, "Удален из друзей");
      } else if (another.requests.contains(user.get_id())) {
        //отношения есть, юзер добавляет в друзья
        another.requests.remove(user.get_id());
        user.subs.remove(another.get_id());

        another.friends.add(user.get_id());
        user.friends.add(another.get_id());

        status = new Status(true, "Друг добавлен");
      } else {
        //отношений нет, юзер добавляется в друзья
        another.subs.add(user.get_id());
        user.requests.add(another.get_id());

        status = new Status(true, "Запрос дружбы оформлен");
      }
      memberService.saveMember(user);
      memberService.saveMember(another);
      return status;
    }else return new Status(false, "Нельзя дружить с собой");
  }

}
