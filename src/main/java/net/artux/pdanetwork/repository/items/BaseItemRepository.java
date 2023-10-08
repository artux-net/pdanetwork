package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.entity.items.BaseItemEntity;
import net.artux.pdanetwork.entity.items.ItemType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BaseItemRepository extends JpaRepository<BaseItemEntity, Long> {

    @Cacheable(value = "base_items")
    List<BaseItemEntity> findAllByType(ItemType type);

}
