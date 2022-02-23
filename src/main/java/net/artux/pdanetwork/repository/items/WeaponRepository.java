package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.models.profile.items.ItemEntity;
import net.artux.pdanetwork.models.profile.items.WeaponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface WeaponRepository extends JpaRepository<WeaponEntity, UUID> {


}
