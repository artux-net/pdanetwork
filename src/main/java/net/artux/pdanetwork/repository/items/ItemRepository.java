package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.models.profile.items.ItemEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Component
public interface ItemRepository extends BaseItemRepository<ItemEntity> {

}
