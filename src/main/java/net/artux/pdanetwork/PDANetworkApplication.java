package net.artux.pdanetwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
@Configuration
public class PDANetworkApplication {

    private static final Logger log =
            LoggerFactory.getLogger(PDANetworkApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PDANetworkApplication.class, args);
        log.debug("Starting my application in debug with {} args", args.length);
        log.info("PDA Network started, swagger: " + "http://localhost:8080/pdanetwork/swagger-ui/", args.length);
    }

    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger("application");
    }
}
