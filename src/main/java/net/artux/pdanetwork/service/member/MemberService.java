package net.artux.pdanetwork.service.member;

import net.artux.pdanetwork.models.Member;
import net.artux.pdanetwork.models.MemberDto;
import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.ResponsePage;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.UserInfo;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;

public interface MemberService {

  Status registerUser(RegisterUser registerUser);
  Status handleConfirmation(String token);
  Status resetData();
  Member getMember();
  MemberDto getMemberDto();
  Member getMember(String base64);
  Member getMember(ObjectId objectId);
  Member getMemberByPdaId(Integer id);
  Member getMemberByEmail(String email);
  Status editMember(RegisterUser user);
  Member saveMember(Member member);
  ResponsePage<UserInfo> getRating(QueryPage queryPage);
  Member doActions(HashMap<String, List<String>> actions);

}
