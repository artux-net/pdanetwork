package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.entity.items.ItemEntity;
import org.springframework.stereotype.Component;

@Component
public interface ItemRepository extends CommonItemRepository<ItemEntity> {

   /* @Query("select w from WeaponEntity w where w.isEquipped is true and w.owner = ?#{ principal?.id }")
    Optional<WearableEntity> findByOwnerAndIsEquippedTrue();*/

}
