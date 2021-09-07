package net.artux.pdanetwork.service.profile;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Member;
import net.artux.pdanetwork.models.MemberMapper;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.models.profile.Achievement;
import net.artux.pdanetwork.service.member.MemberService;
import net.artux.pdanetwork.service.files.AchievementsService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProfileServiceIml implements ProfileService {

  private final MemberService memberService;
  private final MemberMapper memberMapper;
  private final AchievementsService achievementsService;


  @Override
  public Profile getProfile(Integer pdaId) {
    return memberMapper.profile(memberService.getMemberByPdaId(pdaId), memberService.getMember());
  }

  @Override
  public Profile getProfile() {
    return memberMapper.profile(memberService.getMember());
  }

  @Override
  public List<Achievement> getAchievements(Integer pdaId) {
    List<Achievement> achievementsOfMember = new ArrayList<>();
    Member member = memberService.getMemberByPdaId(pdaId);
    for (Achievement achievement : achievementsService.getAchievements()) {
      if (member.achievements.contains(achievement.cid))
        achievementsOfMember.add(achievement);
    }
    return achievementsOfMember;
  }

  @Override
  public List<Achievement> getAchievements() {
    return getAchievements(memberService.getMember().getPdaId());
  }
}
