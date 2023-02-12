package net.artux.pdanetwork.configuration.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.artux.pdanetwork.models.communication.MessageMapper;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.user.ban.BanService;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.stereotype.Service;

@Service
public class RPHandler extends CommonHandler {

    public RPHandler(UserService userService, ObjectMapper objectMapper, MessageMapper messageMapper, ValuesService valuesService, BanService banService) {
        super(userService, objectMapper, messageMapper, valuesService, banService);
    }
}
