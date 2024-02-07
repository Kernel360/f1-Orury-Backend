package org.fastcampus.oruryclient.auth.strategy.applefeign;

import org.fastcampus.oruryclient.global.config.FeignConfig;
import org.fastcampus.orurydomain.auth.dto.apple.AppleOAuthTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(
        name = "apple-auth",
        url = "${oauth-login.client.apple-auth.url}",
        configuration = FeignConfig.class
)
public interface AppleAuthClient {

    @PostMapping("/auth/token")
    AppleOAuthTokenDto getIdToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("grant_type") String grantType,
            @RequestParam("code") String code
    );
}
