package net.artux.pdanetwork.service.items;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.seller.SellerDto;

import java.util.UUID;

public interface SellerService {

    SellerDto getSeller(long id);

    Status add(UUID pdaId, int id, int quantity);

    Status buy(long sellerId, UUID id, int quantity);

    Status sell(long sellerId, UUID id, int quantity);

}
