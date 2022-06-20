package net.artux.pdanetwork.models.user;

import net.artux.pdanetwork.models.gang.GangRelationDto;
import net.artux.pdanetwork.models.gang.GangRelationEntity;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.dto.UserDto;
import net.artux.pdanetwork.models.user.dto.UserInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring") // TODO helpers
public interface UserMapper {

    UserInfoDto info(UserEntity userEntity);

    List<UserInfoDto> info(List<UserEntity> userEntity);

    RegisterUserDto regUser(UserEntity userEntity);

    FriendModel friendModel(UserEntity userEntity);

    List<FriendModel> friendList(List<UserEntity> list);

    @Mapping(target = "relations", expression = "java(mapRelation(userEntity.getGangRelation()))")
    UserDto memberDto(UserEntity userEntity);

    @Mapping(target = "friends", ignore = true)
    @Mapping(target = "subs", ignore = true)
    @Mapping(target = "achievements", ignore = true) // TODO

    @Mapping(target = "relations", expression = "java(mapRelation(userEntity.getGangRelation()))")
    Profile profile(UserEntity userEntity);

    default Profile profile(UserEntity userEntity, UserEntity by) {
        Profile profile = profile(userEntity);
        //profile.setFriendStatus(Profile.getFriendStatus(userEntity, by));
        return profile;
    }

    default GangRelationDto mapRelation(GangRelationEntity value) {
        return new GangRelationDto(value);
    }

}
