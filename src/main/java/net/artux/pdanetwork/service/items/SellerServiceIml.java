package net.artux.pdanetwork.service.items;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.profile.items.ItemEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.repository.items.ArmorRepository;
import net.artux.pdanetwork.repository.items.ItemRepository;
import net.artux.pdanetwork.repository.items.WeaponRepository;
import net.artux.pdanetwork.service.files.SellerManager;
import net.artux.pdanetwork.service.files.ItemProvider;
import net.artux.pdanetwork.service.member.UserService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SellerServiceIml implements SellerService {

    private final UserService userService;
    private final SellerManager sellerManager;
    //private final ActionService actionService; TODO

    private final ItemProvider itemProvider;
    private final ItemRepository itemRepository;
    private final WeaponRepository weaponsRepository;
    private final ArmorRepository armorRepository;

    @Override
    public Status add(Long pdaId, int type, int id, int quantity) {
        if (userService.getMember().getRole().equals("ADMIN")) {
            UserEntity userEntity = userService.getMemberByPdaId(pdaId);
            //actionService.addItem(userEntity, type, id, quantity);
            //memberService.saveMember(userEntity);
            return new Status(true, "Ok");
        } else return new Status(false, "Not admin");
    }

    @Override
    public Status buy(Integer sellerId, int hashCode) {
        UserEntity userEntity = userService.getMember();
        //Status status = actionService.buy(userEntity, hashCode, sellerId);
        //memberService.saveMember(userEntity);
        return new Status();
    }

    @Override
    public Status sell(int hashCode) {
        UserEntity userEntity = userService.getMember();
        //Status status = actionService.sell(userEntity, hashCode);
        //memberService.saveMember(userEntity);
        return new Status();
    }

    @Override
    public Status set(int hashCode) {
        UserEntity userEntity = userService.getMember();
        //Status status = actionService.set(userEntity, hashCode);
        //memberService.saveMember(userEntity);
        return new Status();
    }

    private boolean manageItems(UUID uuid, int type, int id, int quantity) {
    /*try {
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
    }*/
        return false;
    }

    private ItemEntity manageItems(UUID uuid, ItemEntity itemEntity) {
        // itemEntity.setUserId(uuid);
//TODO
   /* if (itemEntity instanceof WeaponEntity)
      return weaponsRepository.save((WeaponEntity)itemEntity);
    else if (itemEntity instanceof ArmorEntity)
      return armorRepository.save((ArmorEntity) itemEntity);
    else if (itemEntity instanceof ArtifactEntity)
      return itemsManager.addArtifact(data, (ArtifactEntity) itemEntity);
    else
      return itemsManager.addItem(data, itemEntity);*/
        return itemEntity;
    }

}
