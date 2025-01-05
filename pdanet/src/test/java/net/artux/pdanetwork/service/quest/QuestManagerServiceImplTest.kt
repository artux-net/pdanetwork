package net.artux.pdanetwork.service.quest

import net.artux.pdanetwork.AbstractTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles(profiles = ["default", "dev", "test"])
class QuestManagerServiceImplTest : AbstractTest() {

    @Autowired
    private lateinit var questManagerService: QuestManagerService

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun readFromGit() {
        val status = questManagerService.readFromGit()
        println(status.description)
    }
}
