package net.artux.pdanetwork.service.profile;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.achievement.AchievementEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.models.user.Profile;
import net.artux.pdanetwork.models.user.UserMapper;
import net.artux.pdanetwork.models.user.dto.AdminEditUserDto;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.achievement.AchievementsService;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.util.PageService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
public class ProfileServiceIml implements ProfileService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final AchievementsService achievementsService;
    private final PageService pageService;

    @Override
    public Profile getProfile(UUID uuid) {
        Profile profile = userMapper.profile(userService.getUserById(uuid), userService.getUserById());
        profile.setRatingPosition(userRepository.ratingPosition(profile.getId()));
        return profile;
    }

    @Override
    public Profile getProfile() {
        Profile profile = userMapper.profile(userService.getUserById());
        profile.setRatingPosition(userRepository.ratingPosition(profile.getId()));
        return profile;
    }

    @Override
    public List<AchievementEntity> getAchievements(UUID pdaId) {
        UserEntity userEntity = userService.getUserById(pdaId);
        return achievementsService.getForUser(userEntity);
    }

    @Override
    public List<AchievementEntity> getAchievements() {
        return getAchievements(userService.getCurrentId());
    }

    @Override
    public ResponsePage<SimpleUserDto> getRating(QueryPage queryPage) {
        Page<UserEntity> memberPage = userRepository.findAll(
                pageService.getPageable(queryPage));
        return pageService.mapDataPageToResponsePage(memberPage, userMapper.info(memberPage.getContent()));
    }

    @Override
    public ResponsePage<SimpleUserDto> getUsersPage(QueryPage queryPage) {
        return ResponsePage.of(userRepository.findAll(pageService.getPageable(queryPage)).map(userMapper::info));
    }

    @Override
    public ResponsePage<SimpleUserDto> findUsers(String query, QueryPage queryPage) {
        UserEntity user = new UserEntity();
        user.setLogin(query);
        user.setNickname(query);
        user.setName(query);
        user.setEmail(query);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("nickname", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("login", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<UserEntity> example = Example.of(user, matcher);

        return ResponsePage.of(userRepository.findAll(example, pageService.getPageable(queryPage))
                .map(userMapper::info));
    }

    public ResponsePage<SimpleUserDto> findUsers(AdminEditUserDto exampleDto, QueryPage queryPage) {
        UserEntity user = new UserEntity();
        user.setLogin(exampleDto.getLogin());
        user.setNickname(exampleDto.getNickname());
        user.setName(exampleDto.getName());
        user.setRole(exampleDto.getRole());
        user.setGang(exampleDto.getGang());
        user.setEmail(exampleDto.getEmail());
        user.setAvatar(exampleDto.getAvatar());
        user.setChatBan(exampleDto.isChatBan());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("nickname", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("login", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("chatBan", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("role", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("gang", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnoreNullValues();

        Example<UserEntity> example = Example.of(user, matcher);
        return ResponsePage.of(userRepository.findAll(example, pageService.getPageable(queryPage))
                .map(userMapper::info));
    }
}
