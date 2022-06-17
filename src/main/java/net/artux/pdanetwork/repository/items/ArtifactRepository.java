package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.models.profile.items.ArtifactEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ArtifactRepository extends BaseItemRepository<ArtifactEntity> {

}
