package net.artux.pdanetwork.configuration.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.artux.pdanetwork.models.communication.ChatUpdate;
import net.artux.pdanetwork.entity.mappers.MessageMapper;
import net.artux.pdanetwork.entity.mappers.UserMapper;
import net.artux.pdanetwork.service.content.ContentService;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.user.ban.BanService;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class RPHandler extends CommonHandler {

    public RPHandler(UserService userService, ObjectMapper objectMapper, MessageMapper messageMapper, ValuesService valuesService, BanService banService, ContentService contentService, UserMapper userMapper) {
        super(userService, objectMapper, messageMapper, valuesService, banService, userMapper);
        Random random = new Random();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                applyUpdate(ChatUpdate.of(contentService.getRandomStalkerMessageDTO()));
            }
        }, 1000 * 60 * (random.nextInt(2) + 2), 1000 * 60 * (random.nextInt(3) + 2));
    }

}
