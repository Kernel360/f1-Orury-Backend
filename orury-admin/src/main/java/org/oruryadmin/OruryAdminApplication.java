package org.oruryadmin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"org.orurycommon", "org.orurydomain", "org.oruryadmin"}, exclude = FlywayAutoConfiguration.class)
public class OruryAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(OruryAdminApplication.class, args);
    }
}
