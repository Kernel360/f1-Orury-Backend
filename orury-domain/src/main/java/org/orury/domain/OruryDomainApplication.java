package org.orury.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.orury.common"})
public class OruryDomainApplication {
    public static void main(String[] args) {
        SpringApplication.run(OruryDomainApplication.class, args);
    }
}
