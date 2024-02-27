package org.orury.domain.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Profile("test")
@TestConfiguration
public class FlywayTestConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    public Flyway flyway() {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration/")
                .cleanOnValidationError(true)
                .baselineOnMigrate(true)
                .initSql("SET FOREIGN_KEY_CHECKS = 0;")
                .load();
    }
}
