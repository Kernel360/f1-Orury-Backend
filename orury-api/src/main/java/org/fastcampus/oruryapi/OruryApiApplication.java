package org.fastcampus.oruryapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"org.fastcampus.orurycore", "org.fastcampus.oruryapi"}, exclude = FlywayAutoConfiguration.class)
public class OruryApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OruryApiApplication.class, args);
    }

}