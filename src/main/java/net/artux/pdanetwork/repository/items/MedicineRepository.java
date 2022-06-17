package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.models.profile.items.MedicineEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface MedicineRepository extends BaseItemRepository<MedicineEntity> {
}
