package org.orury.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"org.orury.common", "org.orury.client", "org.orury.domain"}, exclude = FlywayAutoConfiguration.class)
public class OruryClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(OruryClientApplication.class, args);
    }

}