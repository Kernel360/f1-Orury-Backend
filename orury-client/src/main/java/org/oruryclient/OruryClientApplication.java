package org.oruryclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"org.orurycommon", "org.oruryclient", "org.orurydomain"}, exclude = FlywayAutoConfiguration.class)
public class OruryClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(OruryClientApplication.class, args);
    }

}