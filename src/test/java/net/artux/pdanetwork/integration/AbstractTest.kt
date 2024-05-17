package net.artux.pdanetwork.integration

import org.junit.ClassRule
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File
import java.time.Duration

open class AbstractTest {

    companion object {
        @JvmField
        @ClassRule
        val environment: DockerComposeContainer<*> = DockerComposeContainer(File("docker-compose.yml"))
            .withExposedService("postgres", 5432,
                Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)))
            .withLocalCompose(true)
    }

}