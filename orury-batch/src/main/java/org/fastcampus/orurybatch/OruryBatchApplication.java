package org.fastcampus.orurybatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableBatchProcessing
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"org.fastcampus.orurybatch", "org.fastcampus.orurydomain", "org.fastcampus.orurycommon"}, exclude = FlywayAutoConfiguration.class)
public class OruryBatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(OruryBatchApplication.class, args);
    }
}
