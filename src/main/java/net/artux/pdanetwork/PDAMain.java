package net.artux.pdanetwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PDAMain extends SpringBootServletInitializer {

  static final Logger log =
          LoggerFactory.getLogger(PDAMain.class);

  public static void main(String[] args) {
    SpringApplication.run(PDAMain.class, args);
    log.debug("Starting my application in debug with {} args", args.length);
    log.info("Starting my application with {} args.", args.length);
  }


}
