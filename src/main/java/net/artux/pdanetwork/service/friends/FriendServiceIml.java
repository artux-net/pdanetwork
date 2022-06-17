package net.artux.pdanetwork.service.friends;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.FriendModel;
import net.artux.pdanetwork.models.MemberMapper;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.RelationshipEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.repository.user.RelationshipRepository;
import net.artux.pdanetwork.repository.user.UsersRepository;
import net.artux.pdanetwork.service.member.MemberService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FriendServiceIml implements FriendService {

    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final RelationshipRepository relationshipRepository;
    private final UsersRepository usersRepository;

    @Override
    public List<FriendModel> getFriends(Long pdaId) {
        return memberMapper.friendList(usersRepository.getFriendsById(pdaId));
    }

    @Override
    public List<FriendModel> getSubs() {
        return getSubs(memberService.getMember().getPdaId());
    }

    @Override
    public List<FriendModel> getSubs(Long pdaId) {
        return memberMapper.friendList(usersRepository.getSubsById(pdaId));
    }

    @Override
    public List<FriendModel> getFriends() {
        return getFriends(memberService.getMember().getPdaId());
    }

    @Override
    public List<FriendModel> getFriendRequests() {
        return memberMapper.friendList(usersRepository.getRequestsById(memberService.getMember().getPdaId()));
    }

    @Override
    @Transactional // TODO everywhere
    public Status addFriend(Long pdaId) {
        UserEntity user = memberService.getMember();
        UserEntity another = memberService.getMemberByPdaId(pdaId);
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
