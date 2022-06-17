package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.models.profile.items.ItemEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BaseItemRepository<T extends ItemEntity> extends JpaRepository<T, UUID> {

    Optional<T> findByOwnerAndBaseId(UserEntity userEntity, int baseId);
    void deleteByOwner(UserEntity entity);

}
