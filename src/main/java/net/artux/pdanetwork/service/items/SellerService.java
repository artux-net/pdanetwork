package net.artux.pdanetwork.service.items;

import net.artux.pdanetwork.models.Status;

public interface SellerService {

    Status add(Long pdaId, int type, int id, int quantity);

    Status buy(Integer sellerId, int hashCode);

    Status sell(int hashCode);

    Status set(int hashCode);

}
