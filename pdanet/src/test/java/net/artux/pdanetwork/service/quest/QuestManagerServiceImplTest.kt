package net.artux.pdanetwork.service.quest

import net.artux.pdanetwork.AbstractTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles(profiles = ["default", "dev", "test"])
internal class QuestManagerServiceImplTest : AbstractTest() {

    @Autowired
    protected var questManagerService: QuestManagerService? = null

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun readFromGit() {
        val status = questManagerService!!.readFromGit()
        println(status.description)
    }
}