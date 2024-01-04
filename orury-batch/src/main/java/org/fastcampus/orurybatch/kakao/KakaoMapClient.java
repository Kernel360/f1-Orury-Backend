package org.fastcampus.orurybatch.kakao;

import org.springframework.stereotype.Component;

@Component
public class KakaoMapClient {
//
//    @Value("${spring.kakao.key}")
//    private String env;
//
//    private final String BASE_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
//
//    public String searchKeyword() {
//        WebClient webClient = WebClient.builder()
//                .baseUrl(BASE_URL)
//                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + env)
//                .build();
//
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .queryParam("query", "서울시구로구클라이밍")
//                        .queryParam("page", 1)
//                        .build()
//                )
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//    }
}
