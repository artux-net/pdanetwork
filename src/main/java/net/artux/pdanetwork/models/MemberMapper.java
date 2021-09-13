package net.artux.pdanetwork.models;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = MemberMapperHelper.class)
public interface MemberMapper {

    UserInfo info(Member member);
    List<UserInfo> info(List<Member> member);
    RegisterUser regUser(Member member);
    FriendModel friendModel(Member member);
    MemberDto memberDto(Member member);

    @Mapping(target = "friends", expression = "java(member.getFriends().size())")
    @Mapping(target = "subs", expression = "java(member.getSubs().size())")
    @Mapping(target = "achievements", expression = "java(member.getAchievements().size())")
    Profile profile(Member member);

    default Profile profile(Member member, Member by){
        Profile profile = profile(member);
        profile.setFriendStatus(Profile.getFriendStatus(member, by));
        return profile;
    }

}
