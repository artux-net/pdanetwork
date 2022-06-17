package net.artux.pdanetwork.service.profile;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.ResponsePage;
import net.artux.pdanetwork.models.UserInfo;
import net.artux.pdanetwork.models.achievement.AchievementEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.models.MemberMapper;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.repository.user.UsersRepository;
import net.artux.pdanetwork.service.member.MemberService;
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
  private final MemberService memberService;
  private final MemberMapper memberMapper;
  private final AchievementsService achievementsService;
  private final SortService sortService;
  private final PageService pageService;


  @Override
  public Profile getProfile(Long pdaId) {
    return memberMapper.profile(memberService.getMemberByPdaId(pdaId), memberService.getMember());
  }

  @Override
  public Profile getProfile() {
    return memberMapper.profile(memberService.getMember());
  }

  @Override
  public List<AchievementEntity> getAchievements(Long pdaId) {
    UserEntity userEntity = memberService.getMemberByPdaId(pdaId);
    return achievementsService.getForUser(userEntity);
  }

  @Override
  public List<AchievementEntity> getAchievements() {
    return getAchievements(memberService.getMember().getPdaId());
  }

  @Override
  public ResponsePage<UserInfo> getRating(QueryPage queryPage) {
    Page<UserEntity> memberPage = usersRepository.findAll(
            sortService.getSortInfo(UserInfo.class, queryPage, "xp"));
    return pageService.mapDataPageToResponsePage(memberPage, memberMapper.info(memberPage.getContent()));
  }
}
