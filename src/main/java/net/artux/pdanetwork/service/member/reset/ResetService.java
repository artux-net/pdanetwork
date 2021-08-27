package net.artux.pdanetwork.service.member.reset;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.ResponsePage;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.UserInfo;

public interface ResetService {

  Status sendLetter(String email);
  Status changePassword(String token, String password);

}
