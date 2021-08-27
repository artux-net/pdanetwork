package net.artux.pdanetwork.service.items;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.profile.items.Armor;
import net.artux.pdanetwork.models.profile.items.Item;
import net.artux.pdanetwork.models.profile.items.Weapon;
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
      Member member = memberService.getMemberByPdaId(pdaId);
      actionService.addItem(member, type, id, quantity);
      memberService.saveMember(member);
      return new Status(true, "Ok");
    }else return new Status(false, "Not admin");
  }

  @Override
  public Status buy(Integer sellerId, int hashCode) {
    Member member = memberService.getMember();
    Status status = actionService.buy(member, hashCode, sellerId);
    memberService.saveMember(member);
    return status;
  }

  @Override
  public Status sell(int hashCode) {
    Member member = memberService.getMember();
    Status status = actionService.sell(member, hashCode);
    memberService.saveMember(member);
    return status;
  }

  @Override
  public Status set(int hashCode) {
    Member member = memberService.getMember();
    Status status = actionService.set(member, hashCode);
    memberService.saveMember(member);
    return status;
  }

}
