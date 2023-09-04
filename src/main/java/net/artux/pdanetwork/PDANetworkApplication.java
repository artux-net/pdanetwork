package net.artux.pdanetwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class PDANetworkApplication {

    private static final Logger log =
            LoggerFactory.getLogger(PDANetworkApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PDANetworkApplication.class, args);
        log.debug("Starting my application in debug with {} args", args.length);
    }

    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger("PDANet");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
