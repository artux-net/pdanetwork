package net.artux.pdanetwork.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.service.member.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActionServiceTest {
    private String login = "maxxx";

    @Autowired
    private ActionService actionService;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void actionsLoads() {
        assertThat(actionService).isNotNull();
    }

    @Test
    public void correctWorks() throws JsonProcessingException {
        String[] params = {"param1", "param"};

        UserEntity userEntity = userService.getMemberByLogin(login);

        HashMap<String, List<String>> actions = new HashMap<>();
        actions.put("add", Arrays.asList(params));

        StoryData s = actionService.doUserActions(actions, userEntity);

        assertThat(objectMapper.writeValueAsString(s).contains("param1"));
    }

}
