package org.orury.batch.client;

import lombok.extern.slf4j.Slf4j;
import org.orury.batch.dto.KakaoMapGymResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class KakaoMapClient {
    @Value("${spring.kakao.key}")
    private String env;

    @Value("${spring.kakao.baseurl}")
    private String BASE_URL;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, env)
                .build();
    }

    public KakaoMapGymResponse searchGyms(String location, int page) {
        String queryParam = "서울시" + location + "클라이밍";
        return webClient().get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", queryParam)
                        .queryParam("page", page)
                        .build()
                )
                .retrieve()
                .bodyToMono(KakaoMapGymResponse.class)
                .block();
    }
}
