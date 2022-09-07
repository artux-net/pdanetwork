package net.artux.pdanetwork.models.user;

import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.entity.user.gang.GangRelationEntity;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;
import net.artux.pdanetwork.models.user.dto.UserDto;
import net.artux.pdanetwork.models.user.gang.GangRelationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "achievements", expression = "java(user.getAchievements().size())")
    SimpleUserDto info(UserEntity user);

    List<SimpleUserDto> info(List<UserEntity> users);

    RegisterUserDto regUser(UserEntity user);

    @Mapping(target = "relations", expression = "java(mapRelation(user.getGangRelation()))")
    UserDto dto(UserEntity user);

    @Mapping(target = "friendRelation", ignore = true)
    @Mapping(target = "blocked", ignore = true)
    @Mapping(target = "friends", ignore = true)
    @Mapping(target = "subs", ignore = true)
    @Mapping(target = "achievements", expression = "java(user.getAchievements().size())")
    @Mapping(target = "relations", expression = "java(mapRelation(user.getGangRelation()))")
    Profile profile(UserEntity user);

    default Profile profile(UserEntity user, UserEntity by) {
        Profile profile = profile(user);
        //profile.setFriendStatus(Profile.getFriendStatus(userEntity, by));
        return profile;
    }

    default GangRelationDto mapRelation(GangRelationEntity value) {
        return new GangRelationDto(value);
    }

}
