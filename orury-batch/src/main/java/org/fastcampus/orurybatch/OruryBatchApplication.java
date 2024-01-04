package org.fastcampus.orurybatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"org.fastcampus.orurybatch", "org.fastcampus.orurydomain", "org.fastcampus.orurycommon"}, exclude = FlywayAutoConfiguration.class)
public class OruryBatchApplication {
    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(OruryBatchApplication.class, args)));
    }
}
