package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.entity.items.BaseItemEntity;
import net.artux.pdanetwork.models.items.ItemType;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface BaseItemRepository extends JpaRepository<BaseItemEntity, Long> {

    @Cacheable(value = "base_items")
    List<BaseItemEntity> findAllByType(ItemType type);

    @NotNull
    @EntityGraph(value = "encItem", type = EntityGraph.EntityGraphType.LOAD)
    Optional<BaseItemEntity> findById(@NotNull Long id);
}
