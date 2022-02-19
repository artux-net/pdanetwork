package net.artux.pdanetwork.service.member;

import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.models.MemberDto;
import net.artux.pdanetwork.models.RegisterUser;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.ResponsePage;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.UserInfo;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface MemberService {

  Status registerUser(RegisterUser registerUser);
  Status handleConfirmation(String token);
  MemberDto resetData();
  UserEntity getMember();
  MemberDto getMemberDto();
  UserEntity getMember(String base64);
  UserEntity getMember(UUID objectId);
  UserEntity getMemberByPdaId(Integer id);
  UserEntity getMemberByEmail(String email);
  Status editMember(RegisterUser user);
  UserEntity saveMember(UserEntity userEntity);
  ResponsePage<UserInfo> getRating(QueryPage queryPage);
  UserEntity doActions(HashMap<String, List<String>> actions);

}
