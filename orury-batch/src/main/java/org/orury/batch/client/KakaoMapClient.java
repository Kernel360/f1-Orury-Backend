package org.orury.batch.client;

import lombok.extern.slf4j.Slf4j;
import org.orury.batch.dto.GymResponse;
import org.orury.batch.dto.KakaoMapGymResponse;
import org.orury.batch.util.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<GymResponse> getItems() {
        List<KakaoMapGymResponse> items = new ArrayList<>();
        Constant.LOCATIONS.forEach(location -> {
            for (int page = 1; page <= 3; page++) {
                var response = searchGyms(location, page);
                items.add(response);
                if (response.getMeta()
                        .getIsEnd()) break;
            }
        });

        return items.stream()
                .map(KakaoMapGymResponse::getDocuments)
                .flatMap(List::stream)
                .map(GymResponse::from)
                .collect(Collectors.toList());
    }
}
