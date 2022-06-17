package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.models.profile.items.ArmorEntity;
import net.artux.pdanetwork.models.profile.items.ItemEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface ArmorRepository extends BaseItemRepository<ArmorEntity> {

}
