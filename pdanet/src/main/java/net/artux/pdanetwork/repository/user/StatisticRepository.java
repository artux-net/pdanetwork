package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.entity.user.StatisticEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface StatisticRepository extends JpaRepository<StatisticEntity, UUID> {

    StatisticEntity findByUser_Id(UUID userId);

}
