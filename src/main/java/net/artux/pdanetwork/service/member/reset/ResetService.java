package net.artux.pdanetwork.service.member.reset;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.dto.StoryData;

public interface ResetService {

    Status sendLetter(String email);

    Status changePassword(String token, String password);

    StoryData resetData();

}
