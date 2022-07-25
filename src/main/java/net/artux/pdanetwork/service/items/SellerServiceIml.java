package net.artux.pdanetwork.service.items;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.story.seller.SellerDto;
import net.artux.pdanetwork.models.story.seller.SellerMapper;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.repository.SellerRepository;
import net.artux.pdanetwork.service.CommonItemsRepository;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.service.files.SellerManager;
import net.artux.pdanetwork.service.member.UserService;
import org.springframework.stereotype.Component;
import org.unbescape.css.CssStringEscapeLevel;

@Component
@RequiredArgsConstructor
public class SellerServiceIml implements SellerService {

    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;
    private final UserService userService;
    private final CommonItemsRepository itemsService;

    private final SellerManager sellerManager;
    private final ActionService actionService;

    @Override
    public SellerDto getSeller(long id) {
        return sellerRepository.findById(id).map(sellerMapper::dto).orElseThrow();
    }

    @Override
    public Status add(Long pdaId, ItemType itemType, int id, int quantity) {
        return null;
    }

   /* @Override
    public Status add(Long pdaId, int type, int baseId, int quantity) {
        if (userService.getMember().getRole() == Role.ADMIN) {
            UserEntity userEntity = userService.getMemberByPdaId(pdaId);
            itemsService.addItem(userEntity, type, baseId, quantity);
            return new Status(true, "Ok");
        } else return new Status(false, "Not admin");
        //todo remove from here
    }*/

    @Override
    public Status buy(long sellerId, ItemType itemType, long id) {
        UserEntity userEntity = userService.getMember();
        //Status status = actionService.buy(userEntity, hashCode, sellerId);
        //memberService.saveMember(userEntity);
        return new Status();
    }

    @Override
    public Status sell(long sellerId, ItemType itemType, long id) {
        UserEntity userEntity = userService.getMember();
        //itemsService.deleteItem();//Status status = actionService.sell(userEntity, hashCode);
        //memberService.saveMember(userEntity);
        return new Status();
    }

    @Override
    public Status set(ItemType itemType, long id) {
        UserEntity userEntity = userService.getMember();
        //Status status = actionService.set(userEntity, hashCode);
        //memberService.saveMember(userEntity);
        return new Status();
    }

}
