package net.artux.pdanetwork.service.profile;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.achievement.AchievementEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.models.user.Profile;
import net.artux.pdanetwork.models.user.UserMapper;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.achievement.AchievementsService;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.util.PageService;
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
    public Profile getProfile(UUID pdaId) {
        return userMapper.profile(userService.getUserById(pdaId), userService.getUserById());
    }

    @Override
    public Profile getProfile() {
        return userMapper.profile(userService.getUserById());
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
}
