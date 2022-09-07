package net.artux.pdanetwork.service.friends;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.entity.user.relationship.RelationshipEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.UserMapper;
import net.artux.pdanetwork.models.user.UserRelation;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;
import net.artux.pdanetwork.repository.user.RelationshipRepository;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
public class FriendServiceIml implements FriendService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final RelationshipRepository relationshipRepository;
    private final UserRepository userRepository;

    @Override
    public List<SimpleUserDto> getRelatedUsers(UUID pdaId, UserRelation relation) {
        return switch (relation){
            case SUBSCRIBER -> userMapper.info(userRepository.getSubsById(pdaId));
            default -> userMapper.info(userRepository.getFriendsById(pdaId));
        };
    }

    @Override
    public List<SimpleUserDto> getFriendRequests() {
        return userMapper.info(userRepository.getRequestsById(userService.getCurrentId()));
    }

    @Override
    public Status relateUser(UUID pdaId) {
        UserEntity user = userService.getUserById();
        UserEntity another = userRepository.getById(pdaId);
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
