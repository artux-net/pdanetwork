package net.artux.pdanetwork.service.items;

import net.artux.pdanetwork.models.Status;

public interface ItemsService {

  Status add(int pdaId, int type, int id, int quantity);
  Status buy(Integer sellerId, int hashCode);
  Status sell(int hashCode);
  Status set(int hashCode);

}
