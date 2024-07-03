package net.artux.pdanetwork.entity.mappers;

import net.artux.pdanetwork.entity.user.StatisticEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.entity.user.gang.GangRelationEntity;
import net.artux.pdanetwork.models.user.Profile;
import net.artux.pdanetwork.models.user.UserStatisticDto;
import net.artux.pdanetwork.models.user.dto.AdminUserDto;
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

    @Mapping(target = "login", source = "nickname")
    List<SimpleUserDto> info(List<UserEntity> users);

    @Mapping(target = "login", source = "nickname")
    UserDto dto(UserEntity user);
    AdminUserDto adminDto(UserEntity user);

    @Mapping(target = "ratingPosition", ignore = true)
    @Mapping(target = "friendRelation", ignore = true)
    @Mapping(target = "friends", ignore = true)
    @Mapping(target = "subs", ignore = true)
    @Mapping(target = "achievements", expression = "java(user.getAchievements().size())")
    @Mapping(target = "relations", expression = "java(mapRelation(user.getGangRelation()))")
    @Mapping(target = "login", source = "nickname")
    Profile profile(UserEntity user);

    default Profile profile(UserEntity user, UserEntity by) {
        Profile profile = profile(user);
        //profile.setFriendStatus(Profile.getFriendStatus(userEntity, by));
        return profile;
    }

    GangRelationDto mapRelation(GangRelationEntity value);

    UserStatisticDto dto(StatisticEntity statistic);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    StatisticEntity entity(UserStatisticDto userStatisticDto);
}
