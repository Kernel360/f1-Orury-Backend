package org.orury.admin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"org.orury.common", "org.orury.domain", "org.orury.admin"}, exclude = FlywayAutoConfiguration.class)
public class OruryAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(OruryAdminApplication.class, args);
    }
}
