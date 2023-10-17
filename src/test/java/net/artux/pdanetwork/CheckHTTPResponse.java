package net.artux.pdanetwork;

import net.artux.pdanetwork.controller.rest.admin.quest.AdminQuestsController;
import net.artux.pdanetwork.models.quest.map.GameMapDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CheckHTTPResponse {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AdminQuestsController questController;

    @Test
    @WithAnonymousUser
    public void isEncWorks() {
        ResponseEntity<String> response = restTemplate.getForEntity("/enc",
                String.class);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    @WithAnonymousUser
    public void isCommandsWorks() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/commands/server",
                String.class);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    @WithAnonymousUser
    public void isStatisticClosedForAnonymous() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/admin/statistic",
                String.class);
        Assertions.assertEquals(401, response.getStatusCode().value());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getMaps() {
        GameMapDto[] maps = questController.getMaps();
        System.out.println(Arrays.toString(maps));
    }

}