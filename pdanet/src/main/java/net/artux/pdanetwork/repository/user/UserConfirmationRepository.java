package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.entity.user.UserConfirmationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserConfirmationRepository extends JpaRepository<UserConfirmationEntity, UUID> {


    Optional<UserConfirmationEntity> findByToken(String token);
}
