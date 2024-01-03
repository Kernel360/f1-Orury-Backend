package org.fastcampus.oruryclient;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"org.fastcampus.orurycommon", "org.fastcampus.oruryclient","org.fastcampus.orurydomain"}, exclude = FlywayAutoConfiguration.class)
public class OruryClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(OruryClientApplication.class, args);
    }

}