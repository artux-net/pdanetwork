package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.WearableEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ItemRepository extends CommonItemRepository<ItemEntity> {

    @Query("select w from WeaponEntity w where w.isEquipped is true and w.owner = ?#{ principal?.id }")
    Optional<WearableEntity> findByOwnerAndIsEquippedTrue();

}
