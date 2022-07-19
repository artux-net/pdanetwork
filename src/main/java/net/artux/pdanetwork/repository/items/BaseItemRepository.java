package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseItemRepository<T extends ItemEntity> extends JpaRepository<T, Long> {

    Optional<T> findByOwnerAndBaseId(UserEntity userEntity, int baseId);

    List<T> findAllByOwnerAndType(UserEntity userEntity, ItemType type);

    void deleteByOwner(UserEntity entity);

}
