package net.artux.pdanetwork;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithAnonymousUser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CheckHTTPResponse {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void isEncWorks() {
        ResponseEntity<String> response = restTemplate.getForEntity("/enc",
                String.class);
        Assert.assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    @WithAnonymousUser
    public void isStatisticOpen() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/admin/statistic",
                String.class);
        Assertions.assertEquals(401, response.getStatusCode().value());
    }

}