package net.artux.pdanetwork.service.items;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.profile.items.Armor;
import net.artux.pdanetwork.models.profile.items.Item;
import net.artux.pdanetwork.models.profile.items.Weapon;

public interface ItemsService {

  Status add(int pdaId, int type, int id, int quantity);
  Status buy(Integer sellerId, int hashCode);
  Status sell(int hashCode);
  Status set(int hashCode);

}
