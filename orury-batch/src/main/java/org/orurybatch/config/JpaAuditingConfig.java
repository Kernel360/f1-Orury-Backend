package org.orurybatch.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "org.orurydomain")
@EnableJpaRepositories(basePackages = "org.orurydomain")
@EnableJpaAuditing
public class JpaAuditingConfig {

}