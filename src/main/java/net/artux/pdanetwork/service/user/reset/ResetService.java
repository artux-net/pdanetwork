package net.artux.pdanetwork.service.user.reset;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.dto.StoryData;

import java.util.Collection;

public interface ResetService {

    Status sendResetPasswordLetter(String email);

    Status changePassword(String token, String password);

    StoryData resetData();

    Collection<String> getTokens();

}
