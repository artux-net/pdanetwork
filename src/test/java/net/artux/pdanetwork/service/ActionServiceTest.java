package net.artux.pdanetwork.service;

import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.models.profile.Data;
import net.artux.pdanetwork.service.action.ActionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ActionServiceTest {

    private ActionService actionService;

    @Test
    public void actionsLoads() throws Exception{
        assertThat(actionService).isNotNull();
    }

    @Test
    public void correctWorks() throws Exception{
        String[] params ={"param1", "param"};

        //TODO

        /*UserEntity userEntity = new UserEntity();
        userEntity.setData(new Data());

        HashMap<String, List<String>> actions = new HashMap<>();
        actions.put("add", Arrays.asList(params));

        UserEntity changed = actionService.doUserActions(actions, userEntity);

        assert(changed.getData().parameters.keys.containsAll(Arrays.asList(params)));*/
    }

}
