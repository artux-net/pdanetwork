package net.artux.pdanetwork.models;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = MemberMapperHelper.class)
public interface MemberMapper {

    UserInfo info(UserEntity userEntity);
    List<UserInfo> info(List<UserEntity> userEntity);
    RegisterUser regUser(UserEntity userEntity);
    FriendModel friendModel(UserEntity userEntity);
    MemberDto memberDto(UserEntity userEntity);

    @Mapping(target = "friends", expression = "java(member.getFriends().size())")
    @Mapping(target = "subs", expression = "java(member.getSubs().size())")
    @Mapping(target = "achievements", expression = "java(member.getAchievements().size())")
    Profile profile(UserEntity userEntity);

    default Profile profile(UserEntity userEntity, UserEntity by){
        Profile profile = profile(userEntity);
        profile.setFriendStatus(Profile.getFriendStatus(userEntity, by));
        return profile;
    }

}
