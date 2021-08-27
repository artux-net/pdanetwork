package net.artux.pdanetwork.models;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    UserInfo info(Member member);
    List<UserInfo> info(List<Member> member);
    RegisterUser regUser(Member member);

    @Mapping(target = "friends", expression = "java(member.getFriends().size())")
    @Mapping(target = "requests", expression = "java(member.getFriendRequests().size())")
    @Mapping(target = "achievements", expression = "java(member.getAchievements().size())")
    Profile profile(Member member);

}
