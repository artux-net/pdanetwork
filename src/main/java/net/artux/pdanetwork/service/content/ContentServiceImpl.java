package net.artux.pdanetwork.service.content;

import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.models.user.dto.UserDto;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Service
public class ContentServiceImpl implements ContentService {

    private final Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);
    private String[] messages;
    private String[] names;
    private String[] nicks;
    private final Random random = new Random();

    @PostConstruct
    private void readAll() {
        try {
            Resource messagesResource = new ClassPathResource("config/messages");
            messages = IOUtils.toString(messagesResource.getInputStream(), StandardCharsets.UTF_8).split("\r");

            Resource nameResource = new ClassPathResource("config/names");
            names = IOUtils.toString(nameResource.getInputStream(), StandardCharsets.UTF_8).split("\r");

            Resource nicksResource = new ClassPathResource("config/nicks");
            nicks = IOUtils.toString(nicksResource.getInputStream(), StandardCharsets.UTF_8).split("\r");

            logger.debug("Content read, {} names, {} nicks, {} messages", names.length, nicks.length, messages.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserDto getRandomStalker() {
        UserDto randomStalker = new UserDto();
        randomStalker.setAvatar(random.nextInt(30) + "");
        randomStalker.setName(names[random.nextInt(names.length)]);
        randomStalker.setNickname(nicks[random.nextInt(nicks.length)]);
        randomStalker.setLogin("bot");
        randomStalker.setEmail("bot@artux.net");
        return randomStalker;
    }

    @Override
    public String getRandomStalkerMessage() {
        return messages[random.nextInt(messages.length)];
    }

    @Override
    public MessageDTO getRandomStalkerMessageDTO() {
        return new MessageDTO(getRandomStalker(), getRandomStalkerMessage());
    }
}
