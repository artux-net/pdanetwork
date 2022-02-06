package net.artux.pdanetwork.service.items;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.UserEntity;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.service.member.MemberService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemsServiceIml implements ItemsService {

  private final MemberService memberService;
  private final ActionService actionService;


  @Override
  public Status add(int pdaId, int type, int id, int quantity) {
    if (memberService.getMember().getRole().equals("admin")) {
      UserEntity userEntity = memberService.getMemberByPdaId(pdaId);
      actionService.addItem(userEntity, type, id, quantity);
      memberService.saveMember(userEntity);
      return new Status(true, "Ok");
    }else return new Status(false, "Not admin");
  }

  @Override
  public Status buy(Integer sellerId, int hashCode) {
    UserEntity userEntity = memberService.getMember();
    Status status = actionService.buy(userEntity, hashCode, sellerId);
    memberService.saveMember(userEntity);
    return status;
  }

  @Override
  public Status sell(int hashCode) {
    UserEntity userEntity = memberService.getMember();
    Status status = actionService.sell(userEntity, hashCode);
    memberService.saveMember(userEntity);
    return status;
  }

  @Override
  public Status set(int hashCode) {
    UserEntity userEntity = memberService.getMember();
    Status status = actionService.set(userEntity, hashCode);
    memberService.saveMember(userEntity);
    return status;
  }

}
