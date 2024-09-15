package net.artux.pdanetwork

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableConfigurationProperties
open class PDANetworkApplication

fun main(vararg args: String) {
    SpringApplication.run(PDANetworkApplication::class.java, *args)
}
