package net.artux.pdanetwork.service.friends;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.FriendRelation;
import net.artux.pdanetwork.models.user.FriendRelationEntity;
import net.artux.pdanetwork.models.user.FriendRequestsEntity;
import net.artux.pdanetwork.models.user.FriendsEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.models.FriendModel;
import net.artux.pdanetwork.models.MemberMapper;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.repository.user.FriendRequestsRepository;
import net.artux.pdanetwork.repository.user.FriendsRepository;
import net.artux.pdanetwork.service.member.MemberService;
import net.artux.pdanetwork.service.files.AchievementsService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class FriendServiceIml implements FriendService {

    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final FriendsRepository friendsRepository;
    private final FriendRequestsRepository friendsRequestRepository;

    UserEntity getAnother(long pdaId, FriendsEntity relationEntity) {
        if (relationEntity.getUser1().getPdaId() == pdaId) {
            return relationEntity.getUser2();
        } else
            return relationEntity.getUser1();
    }

    @Override
    public List<FriendModel> getFriends(Integer pdaId) {
        var relationEntities = friendsRepository.getAllByUser1_UidOrUser2_Uid(memberService.getMemberByPdaId(pdaId).getUid());

        List<FriendModel> friendModels = new ArrayList<>();
        for (FriendsEntity entity : relationEntities) {
            friendModels.add(memberMapper.friendModel(getAnother(pdaId, entity)));
        }

        return friendModels;
    }

    @Override
    public List<FriendModel> getSubs() {
        return getSubs(memberService.getMember().getPdaId());
    }

    @Override
    public List<FriendModel> getSubs(Integer pdaId) {
        var subsRequests = friendsRequestRepository
                .getAllByUser2_Uid(memberService.getMemberByPdaId(pdaId).getUid());

        List<FriendModel> friendModels = new ArrayList<>();
        for (FriendRequestsEntity entity : subsRequests) {
            friendModels.add(memberMapper.friendModel(entity.getUser1()));
        }

        return friendModels;
    }

    @Override
    public List<FriendModel> getFriends() {
        return getFriends(memberService.getMember().getPdaId());
    }

    @Override
    public List<FriendModel> getFriendRequests() {
        var subsRequests = friendsRequestRepository
                .getAllByUser1_Uid(memberService.getMember().getUid());

        List<FriendModel> friendModels = new ArrayList<>();
        for (FriendRequestsEntity entity : subsRequests) {
            friendModels.add(memberMapper.friendModel(entity.getUser2()));
        }

        return friendModels;
    }

    @Override
    @Transactional // TODO everywhere
    public Status addFriend(Integer pdaId) {
        UserEntity user = memberService.getMember();
        UserEntity another = memberService.getMemberByPdaId(pdaId);

        if (!user.getUid().equals(another.getUid())) {
            var friends1 = friendsRepository.getByUser1AndUser2(user, another);
            var friends2 = friendsRepository.getByUser1AndUser2(another, user);
            if (friends1.isPresent() || friends2.isPresent()) {
                var friends = friends1.isPresent() ? friends1 : friends2;

                friendsRepository.delete(friends.get());
                friendsRequestRepository.save(new FriendRequestsEntity(another, user));
                return new Status(true, "Удален из контактов");
            } else {
                var request = friendsRequestRepository.getByUser1AndUser2(user, another);
                if (request.isPresent()) {
                    friendsRequestRepository.delete(request.get());
                    return new Status(true, "Запрос отменен");
                }
                request = friendsRequestRepository.getByUser1AndUser2(another, user);
                if (request.isPresent()) {
                    friendsRequestRepository.delete(request.get());
                    friendsRepository.save(new FriendsEntity(user, another));
                    return new Status(true, "Контакт добавлен");
                }
                friendsRequestRepository.save(new FriendRequestsEntity(user, another));
                return new Status(true, "Запрос оформлен");
            }
        } else return new Status(false, "Ошибка");
    }

}
