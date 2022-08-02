package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface CommonItemRepository<T extends ItemEntity> extends JpaRepository<T, Long> {

    Optional<T> findByOwnerAndId(UserEntity userEntity, long id);

    Optional<T> findByOwnerAndBaseId(UserEntity userEntity, long baseId);

    List<T> findAllByOwner(UserEntity userEntity);

    void deleteByOwner(UserEntity entity);

}
