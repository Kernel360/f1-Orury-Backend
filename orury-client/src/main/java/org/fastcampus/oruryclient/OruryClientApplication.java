package org.fastcampus.oruryclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"org.fastcampus.orurycore", "org.fastcampus.oruryclient"}, exclude = FlywayAutoConfiguration.class)
public class oruryclientApplication {

    public static void main(String[] args) {
        SpringApplication.run(oruryclientApplication.class, args);
    }

}