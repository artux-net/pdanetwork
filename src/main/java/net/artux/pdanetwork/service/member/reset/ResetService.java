package net.artux.pdanetwork.service.member.reset;

import net.artux.pdanetwork.models.Status;

public interface ResetService {

  Status sendLetter(String email);
  Status changePassword(String token, String password);

}
