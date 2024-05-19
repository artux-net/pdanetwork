package net.artux.pdanetwork.service.content;

import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.models.user.dto.UserDto;

public interface ContentService {

    UserDto getRandomStalker();

    String getRandomStalkerMessage();

    MessageDTO getRandomStalkerMessageDTO();

}
