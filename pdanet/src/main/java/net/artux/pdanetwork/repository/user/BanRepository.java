package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.entity.user.BanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface BanRepository extends JpaRepository<BanEntity, UUID> {

    List<BanEntity> findAllByUserId(UUID id);

    void deleteAllByUserId(UUID id);
}
