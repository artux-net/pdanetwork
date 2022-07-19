package net.artux.pdanetwork.service.profile;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.models.user.dto.UserInfoDto;
import net.artux.pdanetwork.entity.AchievementEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.models.user.UserMapper;
import net.artux.pdanetwork.models.user.Profile;
import net.artux.pdanetwork.repository.user.UsersRepository;
import net.artux.pdanetwork.service.member.UserService;
import net.artux.pdanetwork.service.files.AchievementsService;
import net.artux.pdanetwork.service.util.PageService;
import net.artux.pdanetwork.service.util.SortService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProfileServiceIml implements ProfileService {

  private final UsersRepository usersRepository;
  private final UserService userService;
  private final UserMapper userMapper;
  private final AchievementsService achievementsService;
  private final SortService sortService;
  private final PageService pageService;


  @Override
  public Profile getProfile(Long pdaId) {
    return userMapper.profile(userService.getMemberByPdaId(pdaId), userService.getMember());
  }

  @Override
  public Profile getProfile() {
    return userMapper.profile(userService.getMember());
  }

  @Override
  public List<AchievementEntity> getAchievements(Long pdaId) {
    UserEntity userEntity = userService.getMemberByPdaId(pdaId);
    return achievementsService.getForUser(userEntity);
  }

  @Override
  public List<AchievementEntity> getAchievements() {
    return getAchievements(userService.getMember().getPdaId());
  }

  @Override
  public ResponsePage<UserInfoDto> getRating(QueryPage queryPage) {
    Page<UserEntity> memberPage = usersRepository.findAll(
            sortService.getSortInfo(UserInfoDto.class, queryPage, "xp"));
    return pageService.mapDataPageToResponsePage(memberPage, userMapper.info(memberPage.getContent()));
  }
}
