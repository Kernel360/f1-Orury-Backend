package org.fastcampus.orurybatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"org.fastcampus.orurycommon", "org.fastcampus.orurybatch"}, exclude = FlywayAutoConfiguration.class)
public class OruryBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(OruryBatchApplication.class, args);
    }

}
