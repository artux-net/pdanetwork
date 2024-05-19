package net.artux.pdanetwork.service.email

import net.artux.pdanetwork.AbstractTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class EmailServiceImlTest : AbstractTest() {
    @Autowired
    var emailServiceIml: EmailServiceIml? = null

    @Disabled
    @Test
    fun sendSimpleMessage() {
        emailServiceIml!!.sendSimpleMessage("maksim.prygunov@gmail.com", "test", "test")
    }
}