package net.artux.pdanetwork.service.quest;

import net.artux.pdanetwork.models.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestManagerServiceImplTest {

    @Autowired
    protected QuestManagerService questManagerService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void readFromGit() {
        Status status = questManagerService.readFromGit();
        System.out
                .println(status.getDescription());

    }
}