package org.fastcampus.orurydomain.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@TestConfiguration
public class TestJpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("test1");
    }
}