package net.artux.pdanetwork.service.items;

import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.story.seller.SellerDto;

public interface SellerService {

    SellerDto getSeller(long id);

    Status add(Long pdaId, ItemType itemType, int id, int quantity);

    Status buy(long sellerId, ItemType itemType, long id);

    Status sell(long sellerId, ItemType type, long id);

    Status set(ItemType type, long id);


}
