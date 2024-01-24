package net.artux.pdanetwork.service.email;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailServiceImlTest {

    @Autowired
    EmailServiceIml emailServiceIml;


    @Disabled
    @Test
    void sendSimpleMessage() {
        emailServiceIml.sendSimpleMessage("maksim.prygunov@gmail.com", "test", "test");
    }

}