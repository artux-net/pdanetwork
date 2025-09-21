package net.artux.pdanetwork.service.email

import net.artux.pdanetwork.AbstractTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class EmailServiceImlTest : AbstractTest() {

    @Autowired
    private lateinit var emailService: EmailService

    @Disabled
    @Test
    fun sendSimpleMessage() {
        emailService
        // emailService.sendSimpleMessage("maksim.prygunov@gmail.com", "test", "test")
    }
}
