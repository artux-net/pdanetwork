package net.artux.pdanetwork.service.member;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.ResponsePage;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.UserInfo;
import net.artux.pdanetwork.models.profile.Achievement;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface MemberService {

  Status registerUser(RegisterUser registerUser);
  Status handleConfirmation(String token);
  Status resetData();
  Member getMember();
  Member getMember(String base64);
  Member getMemberByPdaId(Integer id);
  Member getMemberByEmail(String email);
  Status editMember(RegisterUser user);
  Member saveMember(Member member);
  ResponsePage<UserInfo> getRating(QueryPage queryPage);
  Member doActions(HashMap<String, List<String>> actions);

}
