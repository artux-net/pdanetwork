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
public interface FriendRequestsRepository extends JpaRepository<FriendRequestsEntity, UUID> {

    List<FriendRequestsEntity> getAllByUser1_UidOrUser2_Uid(UUID uuid);

    List<FriendRequestsEntity> getAllByUser1_Uid(UUID uuid);
    List<FriendRequestsEntity> getAllByUser2_Uid(UUID uuid);

    Optional<FriendRequestsEntity> getByUser1AndUser2(UserEntity user1, UserEntity user2);

}
