package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.models.user.FriendRequestsEntity;
import net.artux.pdanetwork.models.user.FriendsEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public interface FriendsRepository extends JpaRepository<FriendsEntity, UUID> {

    List<FriendsEntity> getAllByUser1_UidOrUser2_Uid(UUID uuid);
    Optional<FriendsEntity> getByUser1AndUser2(UserEntity user1, UserEntity user2);

}
