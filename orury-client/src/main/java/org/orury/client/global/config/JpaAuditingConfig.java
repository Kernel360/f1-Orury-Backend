package org.orury.client.global.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "org.orury.domain")
@EnableJpaRepositories(basePackages = "org.orury.domain")
@EnableJpaAuditing
public class JpaAuditingConfig {

}