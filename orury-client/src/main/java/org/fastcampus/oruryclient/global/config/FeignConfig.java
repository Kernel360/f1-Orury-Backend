package org.fastcampus.oruryclient.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.fastcampus.oruryclient")
public class FeignConfig {
}
