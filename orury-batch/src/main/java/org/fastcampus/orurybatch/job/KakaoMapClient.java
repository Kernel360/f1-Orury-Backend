package org.fastcampus.orurybatch.job;

import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurybatch.dto.KakaoMapGymResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class KakaoMapClient {
    @Value("${spring.kakao.key}")
    private String env;

    @Value("${spring.kakao.baseurl}")
    private String BASE_URL;

    public KakaoMapGymResponse searchGyms(String location) {
        String queryParam = "서울시" + location + "클라이밍";
        int page = 1;
        log.info("queryParam: {}, page: {}", queryParam, page);
        WebClient webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + env)
                .build();

        return webClient.get()
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
