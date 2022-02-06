package net.artux.pdanetwork.service;

import net.artux.pdanetwork.models.Member;
import net.artux.pdanetwork.models.profile.Data;
import net.artux.pdanetwork.service.action.ActionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActionServiceTest {

    @Autowired
    private ActionService actionService;

    @Test
    public void actionsLoads() throws Exception{
        assertThat(actionService).isNotNull();
    }

    @Test
    public void correctWorks() throws Exception{
        String[] params ={"param1", "param"};

        Member member = new Member();
        member.setData(new Data());

        HashMap<String, List<String>> actions = new HashMap<>();
        actions.put("add", Arrays.asList(params));

        Member changed = actionService.doUserActions(actions, member);

        assert(changed.getData().parameters.keys.containsAll(Arrays.asList(params)));
    }

}
