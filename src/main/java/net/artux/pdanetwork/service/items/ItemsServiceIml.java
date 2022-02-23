package net.artux.pdanetwork.service.items;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.profile.Data;
import net.artux.pdanetwork.models.profile.items.ArmorEntity;
import net.artux.pdanetwork.models.profile.items.ArtifactEntity;
import net.artux.pdanetwork.models.profile.items.ItemEntity;
import net.artux.pdanetwork.models.profile.items.WeaponEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.repository.items.ArmorRepository;
import net.artux.pdanetwork.repository.items.ItemsRepository;
import net.artux.pdanetwork.repository.items.WeaponRepository;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.service.files.Types;
import net.artux.pdanetwork.service.member.MemberService;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static net.artux.pdanetwork.utills.ServletContext.error;

@Component
@RequiredArgsConstructor
public class ItemsServiceIml implements ItemsService {

  private final MemberService memberService;
  private final ActionService actionService;

  private final Types types;
  private final ItemsRepository itemsRepository;
  private final WeaponRepository weaponsRepository;
  private final ArmorRepository armorRepository;

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

  private boolean manageItems(UUID uuid, int type, int id, int quantity) {
    try {
        //log("try to add item, type: " + type + ", cid: " + id);
        ItemEntity itemEntity = types.getItem(type, id);
        itemEntity.setQuantity(quantity);
        if (quantity<=0){
          return removeItems(data, values);
        }else
          return manageItems(uuid, itemEntity);
    } catch (Exception e) {
      error("Item err", e);
      return false;
    }
  }

  private ItemEntity manageItems(UUID uuid, ItemEntity itemEntity) {
      itemEntity.setUserId(uuid);

    if (itemEntity instanceof WeaponEntity)
      return weaponsRepository.save((WeaponEntity)itemEntity);
    else if (itemEntity instanceof ArmorEntity)
      return armorRepository.save((ArmorEntity) itemEntity);
    else if (itemEntity instanceof ArtifactEntity)
      return itemsManager.addArtifact(data, (ArtifactEntity) itemEntity);
    else
      return itemsManager.addItem(data, itemEntity);

      return false;
    }
  }

}
