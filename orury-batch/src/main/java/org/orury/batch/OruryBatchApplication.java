package org.orury.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableBatchProcessing
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"org.orury.batch", "org.orury.domain", "org.orury.common"}, exclude = FlywayAutoConfiguration.class)
public class OruryBatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(OruryBatchApplication.class, args);
    }
}
