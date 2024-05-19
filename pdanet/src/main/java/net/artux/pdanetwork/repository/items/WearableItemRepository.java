package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.entity.items.WearableEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface WearableItemRepository<T extends WearableEntity> extends CommonItemRepository<T> {

    Optional<T> findByOwnerAndIsEquippedTrue(UserEntity owner);

}
