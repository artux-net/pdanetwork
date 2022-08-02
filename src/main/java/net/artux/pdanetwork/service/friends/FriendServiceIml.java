package net.artux.pdanetwork.service.friends;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.FriendModel;
import net.artux.pdanetwork.models.user.UserMapper;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.entity.user.RelationshipEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.repository.user.RelationshipRepository;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FriendServiceIml implements FriendService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final RelationshipRepository relationshipRepository;
    private final UserRepository userRepository;

    @Override
    public List<FriendModel> getFriends(Long pdaId) {
        return userMapper.friendList(userRepository.getFriendsById(pdaId));
    }

    @Override
    public List<FriendModel> getSubs() {
        return getSubs(userService.getMember().getPdaId());
    }

    @Override
    public List<FriendModel> getSubs(Long pdaId) {
        return userMapper.friendList(userRepository.getSubsById(pdaId));
    }

    @Override
    public List<FriendModel> getFriends() {
        return getFriends(userService.getMember().getPdaId());
    }

    @Override
    public List<FriendModel> getFriendRequests() {
        return userMapper.friendList(userRepository.getRequestsById(userService.getMember().getPdaId()));
    }

    @Override
    @Transactional // TODO everywhere
    public Status addFriend(Long pdaId) {
        UserEntity user = userService.getMember();
        UserEntity another = userService.getMemberByPdaId(pdaId);
        Optional<RelationshipEntity> relationship = relationshipRepository.getByUser1AndUser2(user, another);
        if (relationship.isPresent()) {
            relationshipRepository.delete(relationship.get());
            return new Status(true, "Удален из контактов");
        } else {
            relationshipRepository.save(new RelationshipEntity(user, another));
            return new Status(true, "Контакт добавлен");
        }
    }

}
