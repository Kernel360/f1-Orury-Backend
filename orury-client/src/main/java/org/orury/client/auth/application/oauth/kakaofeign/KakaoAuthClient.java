package org.orury.client.auth.application.oauth.kakaofeign;

import org.orury.client.global.config.FeignConfig;
import org.orury.domain.auth.domain.dto.kakao.KakaoOAuthTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(
        name = "kakao-auth",
        url = "${oauth-login.client.kakao-auth.url}",
        configuration = FeignConfig.class
)
public interface KakaoAuthClient {

    @PostMapping("/oauth/token")
    KakaoOAuthTokenDto getIdToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("code") String code,
            @RequestParam("client_secret") String clientSecret
    );
}
