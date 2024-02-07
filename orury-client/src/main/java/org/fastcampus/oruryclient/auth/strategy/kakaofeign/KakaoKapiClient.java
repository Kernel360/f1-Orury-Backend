package org.fastcampus.oruryclient.auth.strategy.kakaofeign;

import org.fastcampus.oruryclient.global.config.FeignConfig;
import org.fastcampus.orurydomain.auth.dto.kakao.KakaoAccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(
        name = "kakao-kapi",
        url = "${oauth-login.client.kakao-kapi.url}",
        configuration = FeignConfig.class
)
public interface KakaoKapiClient {

    @PostMapping("/v2/user/me")
    KakaoAccountDto getInfo(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Content-type") String contentType
    );
}
